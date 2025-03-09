package org.univartois.security;

import io.quarkus.security.ForbiddenException;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.univartois.annotation.security.HomePermissionsAllowed;
import org.univartois.enums.HomeRoleType;
import org.univartois.service.RoleService;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@HomePermissionsAllowed()
public class HomePermissionInterceptor {


    @Inject
    RoleService roleService;


    @AroundInvoke
    public Object checkPermissions(InvocationContext invocationContext) throws Exception {
//        ignore annotation on class
        Class<?> declaringClass = invocationContext.getMethod().getDeclaringClass();
        if (declaringClass.isAnnotationPresent(HomePermissionsAllowed.class)) {
            return invocationContext.proceed();
        }


        Method method = invocationContext.getMethod();

        HomePermissionsAllowed homePermissionsAllowed = method.getAnnotation(HomePermissionsAllowed.class);

        if (homePermissionsAllowed == null) {
            return invocationContext.proceed();
        }

        String homeIdParamName = homePermissionsAllowed.homeIdParamName();
        String[] allowedPermissions = homePermissionsAllowed.value();

        Object[] params = invocationContext.getParameters();
        Map<String, Object> argsMap = buildArgumentsMap(method, params);

        UUID homeId = UUID.fromString(argsMap.get(homeIdParamName).toString());

        HomeRoleType[] homeRoleTypes = new HomeRoleType[allowedPermissions.length];
        for (int i = 0; i < allowedPermissions.length; i++) {
            homeRoleTypes[i] = HomeRoleType.valueOf(allowedPermissions[i]);
        }

        boolean hasPermission = roleService.hasAnyRoleByHomeId(homeId, homeRoleTypes);
        if (!hasPermission) {
            throw new ForbiddenException("Vous n'avez pas les permissions requises pour accéder à cette resource : " + Arrays.toString(allowedPermissions));
        }

        return invocationContext.proceed();
    }


    private Map<String, Object> buildArgumentsMap(Method method, Object[] parameters) {
        Map<String, Object> argumentsMap = new HashMap<>();
        var parameterNames = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            argumentsMap.put(parameterNames[i].getName(), parameters[i]);
        }

        return argumentsMap;
    }

}
