package me.earth.earthhack.impl.modules.combat.autocrystal.util;

import java.util.Comparator;
import java.util.TreeSet;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.modules.combat.autocrystal.util.CrystalData;
import me.earth.earthhack.impl.util.math.rotation.RotationUtil;

public class RotationComparator
implements Comparator<CrystalData> {
    private final double ex;
    private final double diff;

    public RotationComparator(double exponent, double minRotationDiff) {
        this.ex = exponent;
        this.diff = minRotationDiff;
    }

    @Override
    public int compare(CrystalData o1, CrystalData o2) {
        double angle2;
        double angle1;
        float[] rotations = null;
        if (o1.hasCachedRotations()) {
            angle1 = o1.getAngle();
        } else {
            rotations = new float[]{Managers.ROTATION.getServerYaw(), Managers.ROTATION.getServerPitch()};
            float[] rotations1 = RotationUtil.getRotations(o1.getCrystal());
            angle1 = RotationUtil.angle(rotations, rotations1);
            o1.cacheRotations(rotations1, angle1);
        }
        if (o2.hasCachedRotations()) {
            angle2 = o2.getAngle();
        } else {
            if (rotations == null) {
                rotations = new float[]{Managers.ROTATION.getServerYaw(), Managers.ROTATION.getServerPitch()};
            }
            float[] rotations2 = RotationUtil.getRotations(o2.getCrystal());
            angle2 = RotationUtil.angle(rotations, rotations2);
            o2.cacheRotations(rotations2, angle2);
        }
        if (Math.abs(angle1 - angle2) < this.diff) {
            return o1.compareTo(o2);
        }
        float damage1 = o1.getDamage();
        float damage2 = o2.getDamage();
        float self1 = o1.getSelfDmg();
        float self2 = o2.getSelfDmg();
        o1.setDamage((float)((double)damage1 * Math.pow(1.0 / (angle1 /= 180.0), this.ex)));
        o2.setDamage((float)((double)damage2 * Math.pow(1.0 / (angle2 /= 180.0), this.ex)));
        o1.setSelfDmg((float)((double)self1 * Math.pow(angle1, this.ex)));
        o2.setSelfDmg((float)((double)self2 * Math.pow(angle2, this.ex)));
        int result = o1.compareTo(o2);
        o1.setSelfDmg(self1);
        o2.setSelfDmg(self2);
        o1.setDamage(damage1);
        o2.setDamage(damage2);
        return result;
    }

    public static <T extends CrystalData> TreeSet<T> asSet(double exponent, double diff) {
        return new TreeSet<CrystalData>(new RotationComparator(exponent, diff));
    }
}
