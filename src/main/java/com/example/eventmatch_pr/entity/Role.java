package com.example.eventmatch_pr.entity;

public enum Role {



        ADMIN,
        PRESTATAIRE,
        ResponsableEvent;

        @Override
        public String toString() {
            return name().toLowerCase().replace("_", " ");
        }

        public static Role fromString(String role) {
            try {
                return Role.valueOf(role.toUpperCase().replace(" ", "_"));
            } catch (IllegalArgumentException e) {
                // Gestion des cas où le rôle n'est pas valide
                System.err.println("Rôle inconnu : " + role);
                return null; // Ou une valeur par défaut, comme Role.TRAITEUR
            }
        }

    }





