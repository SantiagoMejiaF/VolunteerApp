package com.constructiveactivists.authenticationmodule.controllers.configuration.constants;

public final class AppConstants {

    public static final String NOT_FOUND_MESSAGE = "no existe en la base de datos.";
    public static final String POSTULATION_SUBJECT = "La postulación con ID";
    public static final String APPROVAL_SUBJECT = "Aprobación de Vinculación a la Organización %s";
    public static final String REJECTION_SUBJECT = "Rechazo de Vinculación a la Organización %s";
    public static final String APPROVAL_MESSAGE = "Nos complace informarle que su solicitud de vinculación a nuestra organización ha sido aprobada.";
    public static final String REJECTION_MESSAGE = "Lamentamos informarle que su solicitud de vinculación a nuestra organización ha sido rechazada.";

    public static final String EMAIL_TEMPLATE = "<html>" +
            "<body style='font-family: Inter, sans-serif; color: #000000;'>" +
            "<div style='background-color: %s; padding: 30px 10px; text-align: center; color: white;'>" +
            "<img src='cid:image' style='height: 150px; vertical-align: middle; margin-right: 20px;' alt='Volunteer App' />" +
            "<span style='font-size: 28px; font-weight: bold;'>%s</span>" +
            "</div>" +
            "<p>Estimado/a %s,</p>" +
            "<p style='color: #000000;'>%s</p>" +
            "<h3 style='color: #306e86; font-weight: bold;'>Aquí puedes ver los detalles de tus datos:</h3>" +
            "<div style='background-color: #dddfe4; padding: 10px; border-radius: 5px;'>%s</div>" +
            "<p style='color: #000000;'>Atentamente,</p>" +
            "<p><span style='font-weight: bold; color: #000000;'><img src='cid:logo' style='height: 50px; vertical-align: middle;' alt='Volunteer App Logo' /> Volunteer App</span></p>" +
            "</body>" +
            "</html>";

    private AppConstants() {
        throw new UnsupportedOperationException("Esta es una clase de constantes y no puede ser instanciada.");
    }
}
