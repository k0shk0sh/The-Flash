package com.fastaccess.tfl.apps;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Kosh on 19/12/15 11:55 AM
 */
@Table(name = "Folder")
public class FolderModel extends AppsModel {
    @Column private long appId;

    public static List<FolderModel> getFolder(long appId) {
        return new Select().from(FolderModel.class).where("appId = ?", appId).execute();
    }

    public static void delete(long id) {
        new Delete().from(FolderModel.class).where("id = ?", id).executeSingle();
    }

    public static void deleteFolder(long appId) {
        new Delete().from(FolderModel.class).where("appId = ?", appId).execute();
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }
}
