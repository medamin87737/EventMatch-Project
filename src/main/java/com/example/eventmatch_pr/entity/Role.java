package com.example.eventmatch_pr.entity;

public enum Role {

    PRESTATAIRE,
    RESPONSABLE;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
    }

    public static Role fromString(String roleStr) {
        if (roleStr == null || roleStr.isEmpty()) {
            return PRESTATAIRE; // Valeur par défaut si aucune correspondance
        }
        try {
            return Role.valueOf(roleStr.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rôle non valide : " + roleStr);
        }
    }
}
