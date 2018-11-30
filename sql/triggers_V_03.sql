DELIMITER $$
CREATE TRIGGER DeleteUserHistoryTrigger
	BEFORE DELETE
		ON Users FOR EACH ROW
	
    BEGIN
		DELETE FROM rent_history WHERE user_email = OLD.user_email;
	END $$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER RentTimeTrigger
	BEFORE INSERT 
		ON Rent_History FOR EACH ROW 
	BEGIN
		SET new.time_rented = NOW();    
    END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER RetTimeTrigger
	BEFORE UPDATE 
		ON Rent_History FOR EACH ROW 
	BEGIN
		SET new.time_returned = NOW();    
    END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER DeleteAddressUsersTrigger
	AFTER UPDATE
		ON Users FOR EACH ROW
	BEGIN
		DECLARE aCount INT(1);
		
        SELECT COUNT(U.user_email) INTO aCount FROM Users U WHERE U.aid = OLD.aid;
        
        IF(aCount = 0) THEN
			 DELETE FROM Address WHERE aid = OLD.aid;
		END IF;
    END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER DeleteAddressCMTrigger
	AFTER UPDATE
		ON Cast_Member FOR EACH ROW
	BEGIN
		DECLARE aCount INT(1);
		
        SELECT COUNT(C.cid) INTO aCount FROM Cast_Member C WHERE C.aid = OLD.aid;
        
        IF(aCount = 0) THEN
			 DELETE FROM Address WHERE aid = OLD.aid;
		END IF;
    END$$
DELIMITER ;



    
 

