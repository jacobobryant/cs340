package shared.dao;

public interface GeneralPurposeToolBuildingFactoryFactoryFactory {
    // note: due to the way we load the jars dynamically, an instance of each
    // factory will be instantiated whether or not it's the factory we end up
    // using. So initialization code should go in the init() method, not in the
    // constructor.

    // If wipe is true, delete any existing data.
    void init(boolean wipe);

    UserDao makeUserDao();
    Dao makeGameDao();
    String getName();

    // technically we're supposed to have these
    // the server probably won't call them though
    void startTransaction();
    void endTransaction();
}
