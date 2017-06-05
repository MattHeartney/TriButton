package showcode.matt.tributton;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.RemoteException;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Manager class for accessing applications stored on the phone.
 */

public class ApplicationListingManager {

    private Context context;
    private PackageManager packageManager;

    public ApplicationListingManager(Context context) {
        this.context = context;
        packageManager = context.getPackageManager();
    }

    public List<ApplicationInfo> getAppList() {
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
    }

    public void getStatsList(final Consumer<List<PackageStats>> callback) {
        List<ApplicationInfo> appList = getAppList();
        List<PackageStats> toReturn = new LinkedList<>();

        // This is a hack, but it's the only real way to access PackageStats
        try {
            Method getPackageSizeInfo = packageManager.getClass().getMethod(
                    "getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            for (ApplicationInfo info : appList) {
                getPackageSizeInfo.invoke(packageManager, info.packageName, new IPackageStatsObserver.Stub() {

                    @Override
                    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                            throws RemoteException {
                        toReturn.add(pStats);
                        //If the list is fully constructed, return it
                        if (toReturn.size() == appList.size()) {
                            callback.accept(toReturn);
                        }
                    }
                });
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPackageLabel(PackageStats stat) {
        try {
            ApplicationInfo app = packageManager.getApplicationInfo(stat.packageName, 0);
            return (String)packageManager.getApplicationLabel(app);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
