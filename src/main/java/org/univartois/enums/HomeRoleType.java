package org.univartois.enums;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public enum HomeRoleType implements Role {
    ADMIN("Administrateur"), CHEF_REPAS("Chef des repas"), PROPOSITION_REPAS("Proposition des repas"), GARDE_MANGER("Garde manger"), MEMBER("Membre");

    @Getter
    private final String value;


    private final Set<HomeRoleType> children = new HashSet<>();

    static {
        ADMIN.children.add(CHEF_REPAS);
        CHEF_REPAS.children.add(GARDE_MANGER);
        GARDE_MANGER.children.add(PROPOSITION_REPAS);
        PROPOSITION_REPAS.children.add(MEMBER);
    }

    HomeRoleType(String value) {
        this.value = value;
    }

    public static class Constants {
        public static final String ADMIN_ROLE = "ADMIN";
        public static final String CHEF_REPAS_ROLE = "CHEF_REPAS";
        public static final String GARDE_MANGER_ROLE = "GARDE_MANGER";
        public static final String PROPOSITION_REPAS_ROLE = "PROPOSITION_REPAS";
        public static final String MEMBER_ROLE = "MEMBER";
    }

    @Override
    public boolean includes(Role role) {
        return this.equals(role) || this.children.stream().anyMatch(child -> child.includes(role));
    }
}
