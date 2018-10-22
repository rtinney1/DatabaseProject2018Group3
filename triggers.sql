DELIMITER $$
CREATE TRIGGER AddEntertainmentTrigger 
	AFTER INSERT 
		ON Entertainment FOR EACH ROW 

	BEGIN
		IF NEW.platform <=> NULL THEN
			INSERT INTO Movies (eid, title, release_date, genre, num_in_stock, awards_won, sequal_id, platform, version) VALUES
				(NEW.eid, NEW.title, NEW.release_date, NEW.genre, NEW.num_in_stock, NEW.awards_won, NEW.sequal_id, NEW.platform, NEW.version);
			
		ELSE
			INSERT INTO Games (eid, title, release_date, genre, num_in_stock, awards_won, sequal_id, platform, version) VALUES
				(NEW.eid, NEW.title, NEW.release_date, NEW.genre, NEW.num_in_stock, NEW.awards_won, NEW.sequal_id, NEW.platform, NEW.version);
		END IF;
    END $$
DELIMITER ;


    
 

