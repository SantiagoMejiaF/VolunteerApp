package com.constructiveactivists.configurationmodule.constants;

public final class AppConstants {

    public static final String ORGANIZATION_MESSAGE_ID = "La organización con el ID ";
    public static final String VOLUNTEER_MESSAGE_ID = "El voluntario con el ID ";
    public static final String COORDINATOR_MESSAGE_ID = "El coordinador con el ID ";
    public static final String MISSION_MEESAGE_ID = "La misión con el ID: ";
    public static final String NOT_FOUND_MESSAGE = " no existe en la base de datos.";
    public static final String POSTULATION_SUBJECT = "La postulación con ID";
    public static final String VOLUNTEER_GROUP_NOT_FOUND = "Grupo de voluntarios no encontrado";
    public static final String APPROVAL_SUBJECT = "Aprobación de Vinculación a la Organización %s";
    public static final String REJECTION_SUBJECT = "Rechazo de Vinculación a la Organización %s";
    public static final String APPROVAL_MESSAGE = "Nos complace informarle que su solicitud de vinculación a nuestra organización ha sido aprobada.";
    public static final String REJECTION_MESSAGE = "Lamentamos informarle que su solicitud de vinculación a nuestra organización ha sido rechazada.";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String GOOGLE_TOKEN_NOT_PROVIDED = "No se proporcionó un token de Google.";
    public static final String INVALID_GOOGLE_TOKEN = "Token de Google inválido.";
    public static final String USER_NOT_AUTHORIZED = "El usuario no está autorizado para esta actividad.";
    public static final String USER_NOT_FOUND = "Usuario no encontrado";
    public static final String VOLUNTEER_NOT_FOUND = "Voluntario no encontrado";
    public static final String ACTIVITY_NOT_FOUND = "Actividad no encontrada";
    public static final String POSTULATION_NOT_FOUND = "Postulación no encontrada";
    public static final String MISSION_NOT_FOUND = "Misión no encontrada";
    public static final String ATTENDANCE_RECORD_NOT_FOUND = "Registro de asistencia no encontrado";
    public static final String CHECK_IN_SUCCESS = "Check-in registrado exitosamente.";
    public static final String CHECK_OUT_SUCCESS = "Check-out registrado exitosamente.";
    public static final String ZONE_PLACE = "America/Bogota";
    public static final String VOLUNTEER_NOT_ACTIVITIES = "El voluntario no tiene actividades completadas.";
    public static final String VOLUNTEER_NOT_AVAIBLE_REVIEWS = "El voluntario tiene actividades completadas, pero no hay reseñas disponibles.";

    public static final String RELACION_ORGANIZACION_VOLUNTARIO_NOT_FOUND = "Relación entre la organización y el voluntario NO encontrada";
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
