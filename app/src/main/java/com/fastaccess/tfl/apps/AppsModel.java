package com.fastaccess.tfl.apps;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.provider.BaseColumns;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kosh on 9/3/2015. copyrights are reserved
 */
@Table(name = "AppsModel", id = BaseColumns._ID)
public class AppsModel extends Model {

    public enum ItemType {
        FOLDER, APP
    }

    @Column @Expose private String appName;
    @Column(unique = true, onUniqueConflicts = Column.ConflictAction.IGNORE, onUniqueConflict = Column.ConflictAction.IGNORE)
    @Expose private String packageName;
    @Column @Expose private String iconPath;
    @Column @Expose private String activityInfoName;
    @Column @Expose private int appPosition;
    @Column @Expose private int countEntry;
    @Column ItemType itemType = ItemType.APP;
    @Column boolean isHidden;
    private List<FolderModel> folderModels;
    private Bitmap bitmap;
    private ComponentName componentName;
    private IconCache iconCache;
    private PackageManager pm;
    private ResolveInfo info;
    private HashMap<Object, CharSequence> labelCache;

    public AppsModel() {}//do not initialize

    public AppsModel(PackageManager pm, ResolveInfo info, IconCache iconCache, HashMap<Object, CharSequence> labelCache) {
        this.packageName = info.activityInfo.applicationInfo.packageName;
        this.componentName = new ComponentName(packageName, info.activityInfo.name);
        this.activityInfoName = info.activityInfo.name;
        this.appName = info.loadLabel(pm).toString();
        iconCache.getTitleAndIcon(this, info, labelCache);
    }

    public PackageManager getPm() {
        return pm;
    }

    public void setPm(PackageManager pm) {
        this.pm = pm;
    }

    public ResolveInfo getInfo() {
        return info;
    }

    public void setInfo(ResolveInfo info) {
        this.info = info;
    }

    public HashMap<Object, CharSequence> getLabelCache() {
        return labelCache;
    }

    public void setLabelCache(HashMap<Object, CharSequence> labelCache) {
        this.labelCache = labelCache;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ComponentName getComponentName() {
        return componentName;
    }

    public void setComponentName(ComponentName componentName) {
        this.componentName = componentName;
    }

    public IconCache getIconCache() {
        return iconCache;
    }

    public void setIconCache(IconCache iconCache) {
        this.iconCache = iconCache;
    }

    public String getActivityInfoName() {
        return activityInfoName;
    }

    public void setActivityInfoName(String activityInfoName) {
        this.activityInfoName = activityInfoName;
    }

    public void setAppPosition(int appPosition) {
        this.appPosition = appPosition;
    }

    public int getAppPosition() {
        return appPosition;
    }

    public int getCountEntry() {
        return countEntry;
    }

    public void setCountEntry(int countEntry) {
        this.countEntry = countEntry;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public List<FolderModel> getFolderModels() {
        return folderModels;
    }

    public void setFolderModels(List<FolderModel> folderModels) {
        this.folderModels = folderModels;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public static void add(List<AppsModel> modelList) {
        if (modelList != null && modelList.size() != 0) {
            ActiveAndroid.beginTransaction();
            try {
                for (AppsModel model : modelList) {
                    model.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }

        }
    }

    public static boolean deleteByPackageName(String packageName) {
        return new Delete().from(AppsModel.class).where("packageName = ?", packageName).execute() != null;
    }

    public static boolean deleteById(long id) {
        return new Delete().from(AppsModel.class).where("id = ?", id).execute() != null;
    }

    public static AppsModel getAppByPackage(String packageName) {
        return new Select().from(AppsModel.class).where("packageName = ?", packageName).executeSingle();
    }

    public static List<AppsModel> getAll() {
        return new Select().from(AppsModel.class).orderBy("appPosition ASC").execute();
    }

    public static List<AppsModel> getAllByUsage() {
        return new Select().from(AppsModel.class).orderBy("countEntry DESC").execute();
    }

    public static void deleteAll() {
        new Delete().from(AppsModel.class).execute();
    }

    public static AppsModel getById(int id) {
        return new Select().from(AppsModel.class).where("id = ?", id).executeSingle();
    }

    public static int countAll() {
        return new Select().from(AppsModel.class).count();
    }

    public static int lastPosition() {
        AppsModel appsModel = new Select().from(AppsModel.class).orderBy("appPosition DESC").limit(1).executeSingle();
        if (appsModel != null) {
            return appsModel.getAppPosition();
        }
        return 0;
    }

    public static void updateEntry(String packageName) {
        if (packageName != null && !packageName.isEmpty()) {
            AppsModel app = getAppByPackage(packageName);
            if (app != null) {
                app.setCountEntry(app.getCountEntry() + 1);
                app.save();
            }
        }
    }

    public static Comparator<AppsModel> sortApps = new Comparator<AppsModel>() {
        @Override
        public int compare(AppsModel one, AppsModel two) {
            return one.getAppName().compareTo(two.getAppName());
        }
    };
}
