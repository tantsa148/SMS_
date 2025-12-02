DROP VIEW IF EXISTS view_log_destinataire;

CREATE VIEW view_log_destinataire AS
SELECT
    log.id AS log_id,
    log.twilio_response,
    log.status,
    log.twilio_error_code,
    log.expediteur_id,
    log.destinataire_id,
    nd.valeur_numero AS destinataire_numero,
    log.message_id,
    log.platform,          -- colonne plateforme
    log.created_at
FROM sms_response_log log
LEFT JOIN numero_destinataire nd
    ON log.destinataire_id = nd.id_numero;

------------------------------------
---- Historique complet
-------------------------------------

CREATE OR REPLACE VIEW vue_historique_sms AS
SELECT
    s.id AS log_id,
    u.username AS utilisateur,
    e.valeur_numero AS expediteur_numero,
    d.valeur_numero AS destinataire_numero,
    m.texte AS message_texte,
    s.status,
    s.twilio_error_code,
    tec.message AS twilio_error_message,
    tec.more_info AS twilio_error_more_info,
    s.twilio_response AS twilio_sid,
    s.platform,            -- ✅ Ajout de la colonne platform
    s.created_at
FROM sms_response_log s
-- joindre twilio_error_codes
LEFT JOIN twilio_error_codes tec ON s.twilio_error_code = tec.code
-- joindre l'expéditeur
LEFT JOIN numero_expediteur e ON s.expediteur_id = e.id_numero
-- joindre le destinataire
LEFT JOIN numero_destinataire d ON s.destinataire_id = d.id_numero
-- joindre le message
LEFT JOIN messages m ON s.message_id = m.id
-- joindre l'utilisateur via la table possede
LEFT JOIN possede p ON e.id_numero = p.id_numero
LEFT JOIN users u ON p.id_utilisateur = u.id;
