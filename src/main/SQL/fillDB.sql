CREATE database library;
use library;

CREATE TABLE books(
          id int AUTO_INCREMENT PRIMARY KEY,
          title varchar(200) NOT NULL,
          author varchar(200) NOT NULL,
          description varchar(1000),
          is_active int NOT NULL DEFAULT 1,
          count int DEFAULT 0
);

INSERT INTO books(title, author, description, is_active, count)
VALUES
    ('Harry Potter and the Sorcerers Stone','Rowling J.K.','Harry Potter has no idea how famous he is. That''s because he''s being raised by his miserable aunt and uncle who are terrified Harry will learn that he''s really a wizard, just as his parents were. But everything changes when Harry is summoned to attend an infamous school for wizards, and he begins to discover some clues about his illustrious birthright. From the surprising way he is greeted by a lovable giant, to the unique curriculum and colorful faculty at his unusual school, Harry finds himself drawn deep inside a mystical world he never knew existed and closer to his own noble destiny.', 1, 2),
    ('Harry Potter and the Chamber of Secrets','Rowling J.K.','Ever since Harry Potter had come home for the summer, the Dursleys had been so mean and hideous that all Harry wanted was to get back to the Hogwarts School for Witchcraft and Wizardry. But just as he’s packing his bags, Harry receives a warning from a strange impish creature who says that if Harry returns to Hogwarts, disaster will strike.',1,4),
    ('Harry Potter and the Prisoner of Azkaban','Rowling J.K.', 'Harry Potter, along with his best friends, Ron and Hermione, is about to start his third year at Hogwarts School of Witchcraft and Wizardry. Harry can''t wait to get back to school after the summer holidays. (Who wouldn''t if they lived with the horrible Dursleys?) But when Harry gets to Hogwarts, the atmosphere is tense. There''s an escaped mass murderer on the loose, and the sinister prison guards of Azkaban have been called in to guard the school...',1,3),
    ('The Hobbit','Tolkien J.R.R.','In a hole in the ground there lived a hobbit. Not a nasty, dirty, wet hole, filled with the ends of worms and an oozy smell, nor yet a dry, bare, sandy hole with nothing in it to sit down on or to eat: it was a hobbit-hole, and that means comfort.',1, 10),
    ('1984','Orwell G.','The novel by George Orwell is the major work towards which all his previous writing has pointed. Critics have hailed it as his "most solid, most brilliant" work. Though the story of Nineteen Eighty-Four takes place thirty-five years hence, it is in every sense timely. The scene is London, where there has been no new housing since 1950 and where the city-wide slums are called Victory Mansions. Science has abandoned Man for the State. As every citizen knows only too well, war is peace.',1, 5),
    ('The Dead Zone','King S.','The Dead Zone is a science fiction thriller novel by Stephen King published in 1979. The story follows Johnny Smith, who awakens from a coma of nearly five years and, apparently as a result of brain damage, now experiences clairvoyant and precognitive visions triggered by touch. When some information is blocked from his perception, Johnny refers to that information as being trapped in the part of his brain that is permanently damaged, "the dead zone." The novel also follows a serial killer in Castle Rock, and the life of rising politician Greg Stillson, both of whom are evils Johnny must eventually face.',1,2),
    ('It','King S.','It is a 1986 horror novel by American author Stephen King. It was his 22nd book and his 17th novel written under his own name. The story follows the experiences of seven children as they are terrorized by an evil entity that exploits the fears of its victims to disguise itself while hunting its prey. "It" primarily appears in the form of Pennywise the Dancing Clown to attract its preferred prey of young children.',1,2)

CREATE TABLE users(
        id int AUTO_INCREMENT PRIMARY KEY,
        username varchar(200) NOT NULL,
        password varchar(200) NOT NULL,
        firstname varchar(200) NOT NULL,
        lastname varchar(200) NOT NULL,
        is_librarian int NOT NULL DEFAULT 0
)

INSERT INTO users(username, password, firstname, lastname, is_librarian)
    VALUES('librarian','12345','Zino','Adidi', 1)

CREATE TABLE library(
                        id int AUTO_INCREMENT PRIMARY KEY,
                        user_id INT UNSIGNED,
                        book_id INT UNSIGNED,
                        date_borrowing DATETIME DEFAULT CURRENT_TIMESTAMP,
                        date_returning DATETIME
);
