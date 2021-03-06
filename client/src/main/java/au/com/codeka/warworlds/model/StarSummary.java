package au.com.codeka.warworlds.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.andengine.entity.Entity;

import au.com.codeka.common.model.BaseBuildRequest;
import au.com.codeka.common.model.BaseBuilding;
import au.com.codeka.common.model.BaseColony;
import au.com.codeka.common.model.BaseCombatReport;
import au.com.codeka.common.model.BaseEmpirePresence;
import au.com.codeka.common.model.BaseFleet;
import au.com.codeka.common.model.BasePlanet;
import au.com.codeka.common.model.BaseStar;
import au.com.codeka.common.protobuf.Messages;

/**
 * A \c StarSummary is a snapshot of information about a star that we can cache for a much
 * longer period of time (i.e. we cache it in the filesystem basically until the full star
 * is fetched). This is so we can do quicker look-ups of things like star names/icons without
 * having to do a full round-trip.
 */
public class StarSummary extends BaseStar {
    private ArrayList<Entity> mAttachedEntities;

    public int getID() {
        return Integer.parseInt(mKey);
    }

    @Override
    protected BasePlanet createPlanet(Messages.Planet pb) {
        Planet p = new Planet();
        if (pb != null) {
            p.fromProtocolBuffer(this, pb);
        }
        return p;
    }

    @Override
    protected BaseColony createColony(Messages.Colony pb) {
        Colony c = new Colony();
        if (pb != null) {
            c.fromProtocolBuffer(pb);
        }
        return c;
    }

    @Override
    protected BaseBuilding createBuilding(Messages.Building pb) {
        Building b = new Building();
        if (pb != null) {
            b.fromProtocolBuffer(pb);
        }
        return b;
    }

    @Override
    protected BaseEmpirePresence createEmpirePresence(Messages.EmpirePresence pb) {
        EmpirePresence ep = new EmpirePresence();
        if (pb != null) {
            ep.fromProtocolBuffer(pb);
        }
        return ep;
    }

    @Override
    protected BaseFleet createFleet(Messages.Fleet pb) {
        Fleet f = new Fleet();
        if (pb != null) {
            f.fromProtocolBuffer(pb);
        }
        return f;
    }

    @Override
    protected BaseBuildRequest createBuildRequest(Messages.BuildRequest pb) {
        BuildRequest br = new BuildRequest();
        if (pb != null) {
            br.fromProtocolBuffer(pb);
        }
        return br;
    }

    @Override
    public BaseCombatReport createCombatReport(Messages.CombatReport pb) {
        CombatReport report = new CombatReport();
        if (pb != null) {
            report.fromProtocolBuffer(pb);
        }
        report.setStarKey(mKey);
        return report;
    }

    @Override
    public BaseStar clone() {
        Messages.Star.Builder star_pb = Messages.Star.newBuilder();
        toProtocolBuffer(star_pb);

        Star clone = new Star();
        clone.fromProtocolBuffer(star_pb.build());
        return clone;
    }

    public List<Entity> getAttachedEntities() {
        if (mAttachedEntities == null) {
            mAttachedEntities = new ArrayList<Entity>();
        }
        return mAttachedEntities;
    }
    public boolean hasAttachedEntities() {
        return mAttachedEntities != null && mAttachedEntities.size() > 0;
    }

    public String getCoordinateString() {
        int offsetX = (int)(mOffsetX / (float) Sector.SECTOR_SIZE * 1000.0f);
        if (mSectorX < 0) {
            offsetX = 1000 - offsetX;
        }
        offsetX /= Sector.PIXELS_PER_PARSEC;
        int offsetY = (int)(mOffsetY / (float) Sector.SECTOR_SIZE * 1000.0f);
        if (mSectorY < 0) {
            offsetY = 1000 - offsetY;
        }
        offsetY /= Sector.PIXELS_PER_PARSEC;
        return String.format(Locale.ENGLISH, "[%d.%02d,%d.%02d]",
                mSectorX, offsetX,
                mSectorY, offsetY);
    }
}
