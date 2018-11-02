DELIMITER $$
CREATE TRIGGER DeleteUserHistoryTrigger
	BEFORE DELETE
		ON Users FOR EACH ROW
	
    BEGIN
		DELETE FROM rent_history WHERE user_email = OLD.user_email;
	END $$
DELIMITER ;
