package org.univartois.utils;

public class Constants {
//    Messages
    /**
     * messages for users operations
     */
    public static final String EMAIL_ALREADY_EXISTS_MSG = "Un utilisateur avec cet email ou nom d'utilisateur existe déjà.";
    public static final String ACCOUNT_NOT_VERIFIED_MSG = "Votre compte n'a pas encore été vérifié. Veuillez vérifier votre e-mail pour activer votre compte.";
    public static final String ACCOUNT_ALREADY_VERIFIED_MSG = "Ce compte a déjà été vérifié.";
    public static final String EMAIL_INVALID_MSG = "Adresse mail invalide. Veuillez créer un compte.";
    public static final String PASSWORD_INVALID_MSG = "Mot de passe invalide !";
    public static final String ACCOUNT_CREATED_MSG = "Votre compte a été créé avec succès.";
    public static final String ACCOUNT_ACTIVATED_MSG = "Votre compte est désormais activé !";
    public static final String TOKEN_INVALID_MSG = "Token invalide, expiré ou déjà utilisé.";
    public static final String RESET_PASSWORD_EMAIL_SENT_MSG = "Veuillez vérifier votre boîte mail pour réinitialiser votre mot de passe.";
    public static final String EMAIL_VERIFICATION_MSG = "Veuillez vérifier votre boîte mail pour valider votre adresse mail.";
    public static final String USER_NOT_FOUND_MSG = "Cet utilisateur n'existe pas.";

    /**
     * messages for homes operations
     */
    public static final String HOME_NOT_FOUND_MSG = "Cette maison n'existe pas.";
    public static final String HOME_ADMIN_LEAVE_CONSTRAINT_MSG = "Vous ne pouvez pas quitter la maison tant qu'il n'y a pas au moins un autre administrateur.";
    public static final String HOME_DETAILS_RETRIEVED_MSG = "Détails de la maison récupérés.";
    public static final String HOME_CREATED_MSG = "La maison a été créée avec succès.";
    public static final String HOME_LEFT_MSG = "Vous avez quitté la maison avec succès.";
    public static final String USER_ALREADY_IN_HOME_MSG = "Cet utilisateur est déjà membre de cette maison.";
    public static final String USER_ADDED_TO_HOME_MSG = "Cet utilisateur est désormais un membre de votre maison";
    public static final String HOME_MEMBERS_RETRIEVED_MSG = "Membres de la maison récupérés.";public static final String HOME_MEMBER_DETAILS_RETRIEVED_MSG = "Détails du membre de la maison récupérés.";
    public static final String HOME_MEMBER_NOT_FOUND_MSG = "Cet utilisateur ne fait pas partie de cette maison.";
    public static final String HOME_MEMBER_UPDATED_MSG = "Infos de ce membre sont mis à jour.";
    public static final String HOME_ADMIN_SELF_UPDATE_ROLE_CONSTRAINT_MSG = "Un administrateur ne peut pas modifier son propre rôle dans la maison.";
    public static final String HOME_ADMIN_SELF_DELETE_FROM_HOME_CONSTRAINT_MSG = "Un administrateur ne peut pas se supprimer lui-même de la maison. Utilisez plutôt l'option 'Quitter une maison'.";
    public static final String HOME_MEMBER_DELETED_MSG = "Ce membre est supprimé de cette maison avec succès.";


//    Validation messages
    public static final String EMAIL_FORMAT_INVALID = "Adresse mail n'est pas valide";
    public static final String EMAIL_NOT_BLANK = "Adresse mail ne doit pas etre vide";
    public static final String PASSWORD_NOT_BLANK = "Password ne doit pas etre vide";
    public static final String FIRSTNAME_NOT_BLANK = "Nom ne doit pas etre vide";
    public static final String LASTNAME_NOT_BLANK = "Prénom ne doit pas etre vide";

    //    EVENTS
    public static final String USER_CREATED_EVENT = "USER_CREATED";
    public static final String FORGOT_PASSWORD_EVENT = "FORGOT_PASSWORD";
    public static final String RESET_PASSWORD_EVENT = "RESET_PASSWORD";

    //    NAMED QUERIES
    public static final String QUERY_FIND_HOMES_BY_USER_ID = "FIND_HOMES_BY_USER_ID";
    public static final String QUERY_FIND_USERS_BY_HOME_ID = "FIND_USERS_BY_HOME_ID";
    public static final String QUERY_FIN_USER_BY_HOME_ID_AND_USER_ID = "FIND_USER_BY_HOME_ID_AND_USER_ID";


}
