package fileDao;

import shared.dao.Dao;
import shared.dao.GeneralPurposeToolBuildingFactoryFactoryFactory;
import shared.dao.UserDao;
import java.io.*;

public class FileDaoFactory implements GeneralPurposeToolBuildingFactoryFactoryFactory {

    @Override
    public void init(boolean wipe) {
        System.out.println("FileDaoFactory init");
        File[] files = {
                new File("/tmp/state.ticket"),
                new File("/tmp/events.ticket"),
                new File("/tmp/users.ticket")
        };

        for(File file : files) {
            if (wipe) { file.delete(); }
            try { file.createNewFile(); } catch (Exception e) {}
        }
    }

    @Override
    public UserDao makeUserDao() {
        return new FileUserDao();
    }

    @Override
    public Dao makeGameDao() {
        return new FileDao();
    }

    @Override
    public String getName() {
        return "file";
    }

    @Override
    public void startTransaction() {

    }

    @Override
    public void endTransaction() {

    }
}
