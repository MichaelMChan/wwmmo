package au.com.codeka.warworlds.model;

import java.util.TreeMap;

import android.content.Context;
import au.com.codeka.BackgroundRunner;
import au.com.codeka.common.model.Design;
import au.com.codeka.common.model.DesignKind;
import au.com.codeka.common.protobuf.Messages;
import au.com.codeka.warworlds.StyledDialog;
import au.com.codeka.warworlds.api.ApiClient;
import au.com.codeka.warworlds.api.ApiException;

public class BuildManager {
    private static BuildManager sInstance = new BuildManager();
    public static BuildManager getInstance() {
        return sInstance;
    }

    private TreeMap<String, Integer> mBuildingDesignCounts;

    private BuildManager() {
        mBuildingDesignCounts = new TreeMap<String, Integer>();
    }

    public void setup(Messages.EmpireBuildingStatistics empire_building_statistics_pb) {
        mBuildingDesignCounts.clear();
        for (Messages.EmpireBuildingStatistics.DesignCount design_count_pb : empire_building_statistics_pb.getCountsList()) {
            mBuildingDesignCounts.put(design_count_pb.getDesignId(), design_count_pb.getNumBuildings());
        }
    }

    public int getTotalBuildingsInEmpire(String designId) {
        if (mBuildingDesignCounts.get(designId) == null) {
            return 0;
        } else {
            return mBuildingDesignCounts.get(designId);
        }
    }

    public void updateNotes(final String buildRequestKey, final String notes) {
        new BackgroundRunner<BuildRequest>() {
            @Override
            protected BuildRequest doInBackground() {
                try {
                    Messages.BuildRequest build = Messages.BuildRequest.newBuilder()
                            .setKey(buildRequestKey)
                            .setNotes(notes)
                            .build();
                    ApiClient.putProtoBuf("buildqueue", build, Messages.BuildRequest.class);
                } catch (ApiException e) {
                }

                return null;
            }
            @Override
            protected void onComplete(BuildRequest buildRequest) {
            }
        }.execute();
    }

    public void build(final Context context, final Colony colony,
                      final Design design, final Building existingBuilding, final int count) {
        Messages.BuildRequest.BUILD_KIND kind;
        if (design.getDesignKind() == DesignKind.BUILDING) {
            kind = Messages.BuildRequest.BUILD_KIND.BUILDING;
        } else {
            kind = Messages.BuildRequest.BUILD_KIND.SHIP;
        }

        Messages.BuildRequest build_request_pb = Messages.BuildRequest.newBuilder()
                .setBuildKind(kind)
                .setStarKey(colony.getStarKey())
                .setColonyKey(colony.getKey())
                .setEmpireKey(colony.getEmpireKey())
                .setDesignName(design.getID())
                .setCount(count)
                .setExistingBuildingKey(existingBuilding == null ? "" : existingBuilding.getKey())
                .build();

        build(context, build_request_pb);
    }

    public void build(final Context context, final Colony colony,
                      final Design design, final int fleetID, final int count, final String upgradeID) {
        Messages.BuildRequest build_request_pb = Messages.BuildRequest.newBuilder()
                    .setBuildKind(Messages.BuildRequest.BUILD_KIND.SHIP)
                    .setStarKey(colony.getStarKey())
                    .setColonyKey(colony.getKey())
                    .setEmpireKey(colony.getEmpireKey())
                    .setDesignName(design.getID())
                    .setExistingFleetId(fleetID)
                    .setCount(count)
                    .setUpgradeId(upgradeID)
                    .build();

        build(context, build_request_pb);
    }

    private void build(final Context context, final Messages.BuildRequest build_request_pb) {
        new BackgroundRunner<BuildRequest>() {
            private int mErrorCode;
            private String mErrorMsg;

            @Override
            protected BuildRequest doInBackground() {
                try {
                    Messages.BuildRequest build = build_request_pb;
                    build = ApiClient.postProtoBuf("buildqueue", build, Messages.BuildRequest.class);

                    BuildRequest br = new BuildRequest();
                    br.fromProtocolBuffer(build);
                    return br;
                } catch (ApiException e) {
                    if (e.getServerErrorCode() > 0) {
                        mErrorCode = e.getServerErrorCode();
                        mErrorMsg = e.getServerErrorMessage();
                    }
                }

                return null;
            }
            @Override
            protected void onComplete(BuildRequest buildRequest) {
                if (mErrorCode > 0) {
                    try {
                        new StyledDialog.Builder(context)
                                        .setTitle("Cannot Build")
                                        .setMessage(mErrorMsg)
                                        .setPositiveButton("Close", true, null)
                                        .create().show();
                    } catch(Exception e) {
                        // we can get a WindowManager.BadTokenException here if the activity has
                        // finished, we should probably do something about it but it's kinda too
                        // late...
                    }
                } else if (buildRequest != null) {
                    StarManager.i.refreshStar(Integer.parseInt(build_request_pb.getStarKey()));
                }
            }
        }.execute();
    }
}
