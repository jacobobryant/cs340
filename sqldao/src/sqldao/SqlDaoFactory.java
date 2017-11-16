package sqldao;

import shared.dao.Dao;
import shared.dao.GeneralPurposeToolBuildingFactoryFactoryFactory;

public class SqlDaoFactory implements GeneralPurposeToolBuildingFactoryFactoryFactory {
    public Dao makeDao() {
        return new SqlDao();
    }

    public String getName() {
        return "sql";
    }

}
