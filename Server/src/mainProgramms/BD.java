package mainProgramms;

import collectionClasses.*;
import org.postgresql.util.PSQLException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.*;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BD {
    private static String urlHost;
    private static String usernameHost;
    private static String passwordHost;
    private static Connection connection;
    private static final int DB_PORT = 5432;

    public static void readHostData(String fileName) {
        try {
            String fileString = Files.readString(Path.of(fileName), StandardCharsets.UTF_8);

            Pattern usernamePattern = Pattern.compile("(?!:)\\w+(?=:\\w+$)");
            Pattern passwordPattern = Pattern.compile("(?!:)\\w+$");

            Matcher usernameMatcher = usernamePattern.matcher(fileString);
            Matcher passwordMatcher = passwordPattern.matcher(fileString);
            if (!(usernameMatcher.find() & passwordMatcher.find())) throw new IOException();
            usernameHost = usernameMatcher.group();
            passwordHost = passwordMatcher.group();
        } catch (FileNotFoundException exception) {
            System.out.println("Файл " + fileName + " не найден");
        } catch (IOException exception) {
            System.out.println("Не удалось распарсить файл" + fileName);
        }
    }

    public static void connectToBD() {
        urlHost = "jdbc:postgresql://localhost:" + DB_PORT + "/studs";
        try {
            connection = DriverManager.getConnection(urlHost, usernameHost, passwordHost);

        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе данных");
        }
    }

    public static void test() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, "s367300", null, new String[]{"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println(tableName);
            }

            PreparedStatement st = connection.prepareStatement("SELECT * FROM printer");
            ResultSet resultSet = st.executeQuery();

            while (resultSet.next()) {
                String column1 = resultSet.getString("id_printer");
                //int column2 = resultSet.getInt("column2");
                // и так далее для каждого столбца

                System.out.println(column1);
            }

        } catch (SQLException exception) {
            System.out.println("Ошибка при создании запроса");
            exception.printStackTrace();
        }
    }

    public static void createTables() {
        try {
            String query = """
                    BEGIN;
                                    
                    CREATE TYPE astartes_category AS ENUM (
                          'SCOUT', 'INCEPTOR', 'CHAPLAIN'
                          );
                      
                      CREATE TYPE melee_weapon AS ENUM (
                          'CHAIN_AXE', 'MANREAPER', 'LIGHTING_CLAW'
                          );
                      
                      CREATE TYPE weapon AS ENUM (
                          'BOLTGUN', 'HEAVY_BOLTGUN', 'BOLT_PISTOL', 'BOLT_RIFLE', 'COMBI_FLAMER'
                          );
                      CREATE TABLE IF NOT EXISTS users
                      (
                          id              SERIAL PRIMARY KEY,
                          name            VARCHAR(40) UNIQUE      NOT NULL,
                          password        VARCHAR(96)             NOT NULL,
                          role            VARCHAR(25)             NOT NULL,
                          creation_date   TIMESTAMP DEFAULT NOW() NOT NULL
                      );
                      
                      CREATE TABLE IF NOT EXISTS chapter
                      (
                          id             SERIAL PRIMARY KEY,
                          chapter_name   VARCHAR(40) NOT NULL,
                          parent_legion  VARCHAR(40),
                          marines_count  BIGINT
                      );
                      ALTER TABLE chapter 
                        ADD CONSTRAINT unique_chapter UNIQUE (chapter_name, parent_legion, marines_count);
                      
                      CREATE TABLE IF NOT EXISTS coordinates
                      (
                          id            SERIAL PRIMARY KEY,
                          x             FLOAT NOT NULL,
                          y             BIGINT NOT NULL
                      ); 
                      ALTER TABLE coordinates 
                        ADD CONSTRAINT unique_coordinates UNIQUE (x, y);
                      
                      CREATE TABLE IF NOT EXISTS space_marines
                      (
                          id            SERIAL PRIMARY KEY,
                          name          VARCHAR(40)             NOT NULL,
                          id_coordinates INT NOT NULL REFERENCES coordinates (id) ON DELETE CASCADE,
                          creation_date TIMESTAMP DEFAULT NOW() NOT NULL,
                          health        INT,
                          category      astartes_category NOT NULL,
                          weapon        weapon NOT NULL,
                          melee_weapon  melee_weapon NOT NULL,
                          id_chapter    INT NOT NULL REFERENCES chapter (id) ON DELETE CASCADE,
                          id_users      INT NOT NULL REFERENCES users (id) ON DELETE CASCADE
                      );
                                    
                    END;""";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException exception) {
            System.out.println("Ошибка при создании таблиц");
        }
    }

    public static void dropTables() {
        try {
            String query = """
                    BEGIN;
                                    
                    drop table IF EXISTS chapter, commands, coordinates, space_marines, users;
                    DROP TYPE IF EXISTS astartes_category, melee_weapon, weapon;
                                    
                    END;""";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException exception) {
            System.out.println("Ошибка при удалении таблиц");
        }
    }

    private static int stupidAddUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO USERS(name, password, role)" +
                            "VALUES (?, ?, 'user') RETURNING id");

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());

            ResultSet result = statement.executeQuery();

            result.next();

            return result.getInt(1);
        } catch (SQLException exception) {
            System.out.println("Ошибка добавления пользователя");
        }
        return 0;
    }

    public static User readUser(int id) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            return new User(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("password"), resultSet.getString("role"), resultSet.getDate("creation_date"));
        } catch (SQLException exception) {
            System.out.println("Ошибка чтения пользователя");
            exception.printStackTrace();
        }
        return null;
    }

    public static int getUserId(User user) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT id FROM users WHERE name = ?");
            st.setString(1, user.getUsername());
            ResultSet resultSet = st.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }

        } catch (SQLException exception) {
            System.out.println("Ошибка чтения пользователя");
            exception.printStackTrace();
        }
        return 0;
    }

    public static int addCoordiantes(Coordinates coordinates) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO coordinates(x, y) VALUES (?, ?) ON CONFLICT (x, y) DO UPDATE SET x = coordinates.x, y = coordinates.y RETURNING id");

            statement.setDouble(1, coordinates.getX());
            statement.setLong(2, coordinates.getY());

            ResultSet result = statement.executeQuery();

            result.next();

            return result.getInt(1);
        } catch (SQLException exception) {
            System.out.println("Ошибка добавления coordinates");
        }
        return 0;
    }

    public static int addChapter(Chapter chapter) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO chapter(chapter_name, parent_legion, marines_count) VALUES (?, ?, ?) ON CONFLICT (chapter_name, parent_legion, marines_count) DO UPDATE SET chapter_name = chapter.chapter_name, parent_legion = chapter.parent_legion, marines_count = chapter.marines_count RETURNING id");

            statement.setString(1, chapter.getName());
            statement.setString(2, chapter.getParentLegion());
            statement.setLong(3, chapter.getMarinesCount());

            ResultSet result = statement.executeQuery();

            result.next();

            return result.getInt(1);
        } catch (SQLException exception) {
            System.out.println("Ошибка добавления chapter");
        }
        return 0;
    }

    public static int addSpaceMarine(SpaceMarine spaceMarine) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO space_marines(name, id_coordinates, creation_date, health, category, weapon, melee_weapon, id_chapter, id_users)" +
                            "VALUES (?, ?, ?, ?, ?::astartes_category, ?::weapon, ?::melee_weapon, ?, ?) RETURNING id");

            statement.setString(1, spaceMarine.getName());
            statement.setInt(2, addCoordiantes(spaceMarine.getCoordinates()));
            statement.setTimestamp(3, Timestamp.from(spaceMarine.getCreationDate().toInstant()));
            statement.setInt(4, spaceMarine.getHealth());
            statement.setString(5, spaceMarine.getCategory().toString());
            statement.setString(6, spaceMarine.getWeaponType().toString());
            statement.setString(7, spaceMarine.getMeleeWeapon().toString());
            statement.setInt(8, addChapter(spaceMarine.getChapter()));
            statement.setInt(9, addUser(spaceMarine.getUser()));

            ResultSet result = statement.executeQuery();

            result.next();

            return result.getInt(1);
        } catch (SQLException exception) {
            System.out.println("Ошибка добавления объекта");
            exception.printStackTrace();
        }
        return 0;
    }

    public static int addUser(User user) {
        int userId = getUserId(user);
        if (userId == 0) {
            return stupidAddUser(user);
        }
        User compUser = readUser(userId);
        if (compUser.getPassword().equals(user.getPassword())) {
            return userId;
        }
        return 0;
    }

    private static Chapter readChapter(int id) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM chapter WHERE id = ?");
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();

            resultSet.next();

            return new Chapter(resultSet.getString("chapter_name"), resultSet.getString("parent_legion"), resultSet.getLong("marines_count"));
        } catch (SQLException exception) {
            System.out.println("Ошибка чтения chapter");
            exception.printStackTrace();
        }
        return null;
    }

    private static Coordinates readCoordinates(int id) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM coordinates WHERE id = ?");
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();

            resultSet.next();

            return new Coordinates(resultSet.getDouble("x"), resultSet.getLong("y"));
        } catch (SQLException exception) {
            System.out.println("Ошибка чтения coordinates");
            exception.printStackTrace();
        }
        return null;
    }

    public static LinkedList<SpaceMarine> readCollection() {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM space_marines");
            ResultSet resultSet = st.executeQuery();

            LinkedList<SpaceMarine> spaceMarines = new LinkedList<>(new CopyOnWriteArrayList<>());

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int id_coordinates = resultSet.getInt("id_coordinates");
                ZonedDateTime creationDate = resultSet.getTimestamp("creation_date").toInstant().atZone(ZoneId.systemDefault());
                int health = resultSet.getInt("health");
                AstartesCategory category = AstartesCategory.valueOf(resultSet.getString("category"));
                Weapon weapon = Weapon.valueOf(resultSet.getString("weapon"));
                MeleeWeapon meleeWeapon = MeleeWeapon.valueOf(resultSet.getString("melee_weapon"));
                int id_chapter = resultSet.getInt("id_chapter");
                int id_users = resultSet.getInt("id_users");

                SpaceMarine addingSpaceMarine = new SpaceMarine(id, name, readCoordinates(id_coordinates), creationDate, health, category, weapon, meleeWeapon, readChapter(id_chapter));
                addingSpaceMarine.setUser(readUser(id_users));
                spaceMarines.add(addingSpaceMarine);
            }
            return spaceMarines;

        } catch (SQLException exception) {
            System.out.println("Ошибка чтения коллекции");
            exception.printStackTrace();
        }
        return null;
    }

    public static int clear(User user) {
        try {
            PreparedStatement st = connection.prepareStatement("delete FROM space_marines WHERE id_users = ?");
            st.setInt(1, getUserId(user));
            return st.executeUpdate();
        } catch (SQLException exception) {
            System.out.println("Ошибка удаления");
            exception.printStackTrace();
        }
        return 0;
    }

    public static int removeAllByWeaponType(User user, Weapon weapon) {
        try {
            PreparedStatement st = connection.prepareStatement("delete FROM space_marines WHERE weapon = ?::weapon AND id_users = ?");
            st.setString(1, weapon.toString());
            st.setInt(2, getUserId(user));
            return st.executeUpdate();
        } catch (SQLException exception) {
            System.out.println("Ошибка удаления");
            exception.printStackTrace();
        }
        return 0;
    }

    public static int removeById(User user, int id) {
        try {
            PreparedStatement st = connection.prepareStatement("delete FROM space_marines WHERE id = ? AND id_users = ?");
            st.setInt(1, id);
            st.setInt(2, getUserId(user));
            return st.executeUpdate();
        } catch (SQLException exception) {
            System.out.println("Ошибка удаления");
            exception.printStackTrace();
        }
        return 0;
    }

    public static int removeGreater(User user, SpaceMarine compSpaceMarine) {
        try {
            PreparedStatement st = connection.prepareStatement("delete FROM space_marines WHERE id > ? and id_users = ?");
            st.setInt(1, compSpaceMarine.getId());
            st.setInt(2, getUserId(user));
            return st.executeUpdate();
        } catch (SQLException exception) {
            System.out.println("Ошибка удаления");
            exception.printStackTrace();
        }
        return 0;
    }

    public static int updateId(User user, SpaceMarine spaceMarine, int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE space_marines SET name = ?, id_coordinates = ?, creation_date = ?, health = ?, category = ?::astartes_category, weapon = ?::weapon, melee_weapon = ?::melee_weapon, id_chapter = ? WHERE id = ? and id_users = ?");


            statement.setString(1, spaceMarine.getName());
            statement.setInt(2, addCoordiantes(spaceMarine.getCoordinates()));
            statement.setDate(3, new Date(Date.from(spaceMarine.getCreationDate().toInstant()).getTime()));
            statement.setInt(4, spaceMarine.getHealth());
            statement.setString(5, spaceMarine.getCategory().toString());
            statement.setString(6, spaceMarine.getWeaponType().toString());
            statement.setString(7, spaceMarine.getMeleeWeapon().toString());
            statement.setInt(8, addChapter(spaceMarine.getChapter()));
            statement.setInt(9, id);
            statement.setInt(10, getUserId(user));
            return statement.executeUpdate();
        } catch (SQLException exception) {
            System.out.println("Ошибка удаления");
            exception.printStackTrace();
        }
        return 0;
    }
}
