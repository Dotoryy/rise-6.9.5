package com.alan.clients.util.rotation;

import com.alan.clients.util.math.MathOperation;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.util.type.IndexedEvictingList;
import com.alan.clients.util.vector.Vector3d;
import java.util.function.Supplier;
import net.minecraft.util.MathHelper;

public enum RotationMetric
{
    ID(() -> Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).getId())),
    PLAYER_X(() -> Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ih())),
    PLAYER_Y(() -> Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ii())),
    PLAYER_Z(() -> Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ij())),
    PLAYER_YAW(() -> Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ik())),
    PLAYER_PITCH(() -> Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).il())),
    TARGET_X(() -> Double.valueOf(((RotationSample)RotationSamples.ZJ.jq()).ih())),
    TARGET_Y(() -> Double.valueOf(((RotationSample)RotationSamples.ZJ.jq()).ii())),
    TARGET_Z(() -> Double.valueOf(((RotationSample)RotationSamples.ZJ.jq()).ij())),
    TARGET_YAW(() -> Double.valueOf(((RotationSample)RotationSamples.ZJ.jq()).ik())),
    TARGET_PITCH(() -> Double.valueOf(((RotationSample)RotationSamples.ZJ.jq()).il())),
    DIFFERENCE_X(() -> Double.valueOf(MathOperation.DIFFERENCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ih()), Double.valueOf(((RotationSample)RotationSamples.ZJ.jq()).ih()) }))),
    DIFFERENCE_Y(() -> Double.valueOf(MathOperation.DIFFERENCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ii()), Double.valueOf(((RotationSample)RotationSamples.ZJ.jq()).ii()) }))),
    DIFFERENCE_Z(() -> Double.valueOf(MathOperation.DIFFERENCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ij()), Double.valueOf(((RotationSample)RotationSamples.ZJ.jq()).ij()) }))),
    PLAYER_PERFECT_YAW(() -> Double.valueOf(RotationUtil.c(new Vector3d(((RotationSample)RotationSamples.ZI.jq()).ih(), 0.0, ((RotationSample)RotationSamples.ZI.jq()).ij()), new Vector3d(((RotationSample)RotationSamples.ZJ.jq()).ih(), 0.0, ((RotationSample)RotationSamples.ZJ.jq()).ij())).getX())),
    PLAYER_PERFECT_PITCH(() -> Double.valueOf(RotationUtil.c(new Vector3d(((RotationSample)RotationSamples.ZI.jq()).ih(), ((RotationSample)RotationSamples.ZI.jq()).ii(), ((RotationSample)RotationSamples.ZI.jq()).ij()), new Vector3d(((RotationSample)RotationSamples.ZJ.jq()).ih(), ((RotationSample)RotationSamples.ZJ.jq()).ii(), ((RotationSample)RotationSamples.ZJ.jq()).ij())).getY())),
    PLAYER_DELTA_X(() -> Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ih() - ((RotationSample)RotationSamples.ZI.get(1)).ih())),
    PLAYER_DELTA_Y(() -> Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ii() - ((RotationSample)RotationSamples.ZI.get(1)).ii())),
    PLAYER_DELTA_Z(() -> Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ij() - ((RotationSample)RotationSamples.ZI.get(1)).ij())),
    PLAYER_DELTA_YAW(() -> Double.valueOf(MathOperation.WRAPPED_TO_180_DISTANCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ik()), Double.valueOf(((RotationSample)RotationSamples.ZI.get(1)).ik()) }))),
    PLAYER_DELTA_PITCH(() -> Double.valueOf(MathOperation.DIFFERENCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).il()), Double.valueOf(((RotationSample)RotationSamples.ZI.get(1)).il()) }))),
    PLAYER_YAW_DIFFERENCE_FROM_PERFECT_YAW(() -> Double.valueOf(MathOperation.WRAPPED_TO_180_DISTANCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ik()), RotationMetric.PLAYER_PERFECT_YAW.io() }))),
    ENEMY_YAW_DIFFERENCE_FROM_PERFECT_YAW(() -> RotationMetric.PLAYER_YAW_DIFFERENCE_FROM_PERFECT_YAW.t(true)),
    PLAYER_PITCH_DIFFERENCE_FROM_PERFECT_PITCH(() -> Double.valueOf(MathOperation.DIFFERENCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).il()), RotationMetric.PLAYER_PERFECT_PITCH.io() }))),
    ENEMY_PITCH_DIFFERENCE_FROM_PERFECT_PITCH(() -> RotationMetric.PLAYER_PITCH_DIFFERENCE_FROM_PERFECT_PITCH.t(true)),
    HORIZONTAL_DISTANCE(() -> Double.valueOf(MathOperation.EUCLIDEAN_DISTANCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZJ.jq()).ih() - ((RotationSample)RotationSamples.ZI.jq()).ih()), Double.valueOf(((RotationSample)RotationSamples.ZJ.jq()).ij() - ((RotationSample)RotationSamples.ZI.jq()).ij()) }))),
    VERTICAL_DISTANCE(() -> Double.valueOf(MathOperation.DIFFERENCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZJ.jq()).ii()), Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ii()) }))),
    PLAYER_CONTRIBUTION_TO_DELTA_HORIZONTAL_DISTANCE(() -> Double.valueOf((double)RotationMetric.HORIZONTAL_DISTANCE.io() - MathOperation.EUCLIDEAN_DISTANCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZJ.jq()).ih() - ((RotationSample)RotationSamples.ZI.get(1)).ih()), Double.valueOf(((RotationSample)RotationSamples.ZJ.jq()).ij() - ((RotationSample)RotationSamples.ZI.get(1)).ij()) }))),
    ENEMY_CONTRIBUTION_TO_DELTA_HORIZONTAL_DISTANCE(() -> RotationMetric.PLAYER_CONTRIBUTION_TO_DELTA_HORIZONTAL_DISTANCE.t(true)),
    PLAYER_CONTRIBUTION_TO_DELTA_VERTICAL_DISTANCE(() -> Double.valueOf(MathOperation.DIFFERENCE.a(new Double[] { Double.valueOf(MathOperation.DIFFERENCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZJ.jq()).ii()), Double.valueOf(((RotationSample)RotationSamples.ZI.get(1)).ii()) })), RotationMetric.VERTICAL_DISTANCE.io() }))),
    ENEMY_CONTRIBUTION_TO_DELTA_VERTICAL_DISTANCE(() -> RotationMetric.PLAYER_CONTRIBUTION_TO_DELTA_VERTICAL_DISTANCE.t(true)),
    ANGLE_OF_CROSS_HAIR_TO_ENEMY(() -> Double.valueOf(MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2((double)RotationMetric.PLAYER_YAW_DIFFERENCE_FROM_PERFECT_YAW.io(), (double)RotationMetric.PLAYER_PITCH_DIFFERENCE_FROM_PERFECT_PITCH.io()))))),
    DELTA_HORIZONTAL_ANGLE_TO_ENEMY(() -> Double.valueOf(MathOperation.WRAPPED_TO_180_DISTANCE.a(new Double[] { RotationMetric.PLAYER_PERFECT_YAW.io(), RotationMetric.PLAYER_PERFECT_YAW.C(1) }))),
    DELTA_VERTICAL_ANGLE_TO_ENEMY(() -> Double.valueOf(MathOperation.DIFFERENCE.a(new Double[] { RotationMetric.PLAYER_PERFECT_PITCH.io(), RotationMetric.PLAYER_PERFECT_PITCH.C(1) }))),
    PLAYER_HORIZONTAL_DELTA(() -> Double.valueOf(MathOperation.EUCLIDEAN_DISTANCE.a(new Double[] { Double.valueOf(MathOperation.DIFFERENCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ih()), Double.valueOf(((RotationSample)RotationSamples.ZI.get(1)).ih()) })), Double.valueOf(MathOperation.DIFFERENCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ij()), Double.valueOf(((RotationSample)RotationSamples.ZI.get(1)).ij()) })) }))),
    PLAYER_HORIZONTAL_MOVEMENT_ANGLE(() -> Double.valueOf(MathHelper.wrapAngleTo180_float((float)(360.0 - Math.atan2(RotationMetric.PLAYER_DELTA_X.io(), RotationMetric.PLAYER_DELTA_Z.io()) * 57.29577951308232 - 180.0)) + 180.0f)),
    PLAYER_FORWARD_PERCENTAGE(() -> Double.valueOf(Math.min(Math.abs(MathOperation.WRAPPED_TO_180_DISTANCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ik()), Double.valueOf((double)RotationMetric.PLAYER_HORIZONTAL_MOVEMENT_ANGLE.io() - 90.0) })), Math.abs(MathOperation.WRAPPED_TO_180_DISTANCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ik()), Double.valueOf((double)RotationMetric.PLAYER_HORIZONTAL_MOVEMENT_ANGLE.io() + 90.0) }))) / 90.0 * 100.0)),
    PLAYER_FORWARD_DELTA(() -> Double.valueOf(RotationMetric.PLAYER_HORIZONTAL_DELTA.io() * RotationMetric.PLAYER_FORWARD_PERCENTAGE.io() / 100.0 * (double)((Math.abs(MathOperation.WRAPPED_TO_180_DISTANCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ik()), RotationMetric.PLAYER_HORIZONTAL_MOVEMENT_ANGLE.io() })) > 90.0) ? -1 : 1))),
    PLAYER_LATERAL_DELTA(() -> Double.valueOf(RotationMetric.PLAYER_HORIZONTAL_DELTA.io() * (1.0 - RotationMetric.PLAYER_FORWARD_PERCENTAGE.io() / 100.0) * (double)((Math.abs(MathOperation.WRAPPED_TO_180_DISTANCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).ik() + 90.0), RotationMetric.PLAYER_HORIZONTAL_MOVEMENT_ANGLE.io() })) > 90.0) ? -1 : 1))),
    ENEMY_FORWARD_DELTA(() -> RotationMetric.PLAYER_FORWARD_DELTA.t(true)),
    ENEMY_LATERAL_DELTA(() -> RotationMetric.PLAYER_LATERAL_DELTA.t(true)),
    PLAYER_CONTRIBUTION_TO_DELTA_HORIZONTAL_ANGLE_TO_ENEMY(() -> Double.valueOf(MathOperation.WRAPPED_TO_180_DISTANCE.a(new Double[] { RotationMetric.PLAYER_PERFECT_YAW.io(), Double.valueOf(RotationUtil.c(new Vector3d(((RotationSample)RotationSamples.ZI.get(1)).ih(), ((RotationSample)RotationSamples.ZI.get(1)).ii(), ((RotationSample)RotationSamples.ZI.get(1)).ij()), new Vector3d(((RotationSample)RotationSamples.ZJ.jq()).ih(), ((RotationSample)RotationSamples.ZJ.jq()).ii(), ((RotationSample)RotationSamples.ZJ.jq()).ij())).getX()) }))),
    ENEMY_CONTRIBUTION_TO_DELTA_HORIZONTAL_ANGLE_TO_PLAYER(() -> RotationMetric.PLAYER_CONTRIBUTION_TO_DELTA_HORIZONTAL_ANGLE_TO_ENEMY.t(true)),
    PLAYER_CONTRIBUTION_TO_DELTA_VERTICAL_ANGLE_TO_ENEMY(() -> Double.valueOf(MathOperation.DIFFERENCE.a(new Double[] { RotationMetric.PLAYER_PERFECT_PITCH.io(), Double.valueOf(RotationUtil.c(new Vector3d(((RotationSample)RotationSamples.ZI.get(1)).ih(), ((RotationSample)RotationSamples.ZI.get(1)).ii(), ((RotationSample)RotationSamples.ZI.get(1)).ij()), new Vector3d(((RotationSample)RotationSamples.ZJ.jq()).ih(), ((RotationSample)RotationSamples.ZJ.jq()).ii(), ((RotationSample)RotationSamples.ZJ.jq()).ij())).getY()) }))),
    ENEMY_CONTRIBUTION_TO_DELTA_VERTICAL_ANGLE_TO_PLAYER(() -> Double.valueOf((double)RotationMetric.PLAYER_CONTRIBUTION_TO_DELTA_VERTICAL_ANGLE_TO_ENEMY.t(true) * -1.0)),
    PLAYER_DELTA_ROTATION(() -> Double.valueOf(MathOperation.EUCLIDEAN_DISTANCE.a(new Double[] { RotationMetric.PLAYER_DELTA_YAW.io(), RotationMetric.PLAYER_DELTA_PITCH.io() }))),
    PLAYER_ROTATION_DISTANCE_FROM_PERFECT_ROTATIONS(() -> Double.valueOf(MathOperation.EUCLIDEAN_DISTANCE.a(new Double[] { RotationMetric.PLAYER_YAW_DIFFERENCE_FROM_PERFECT_YAW.io(), RotationMetric.PLAYER_PITCH_DIFFERENCE_FROM_PERFECT_PITCH.io() }))),
    PLAYER_DELTA_ROTATION_DISTANCE_FROM_PERFECT_ROTATIONS(() -> Double.valueOf(MathOperation.DIFFERENCE.a(new Double[] { RotationMetric.PLAYER_ROTATION_DISTANCE_FROM_PERFECT_ROTATIONS.io(), RotationMetric.PLAYER_ROTATION_DISTANCE_FROM_PERFECT_ROTATIONS.C(1) }))),
    PLAYER_CONTRIBUTION_TO_DELTA_ROTATION_DISTANCE_FROM_PERFECT_ROTATIONS(() -> Double.valueOf(MathOperation.DIFFERENCE.a(new Double[] { RotationMetric.PLAYER_ROTATION_DISTANCE_FROM_PERFECT_ROTATIONS.io(), Double.valueOf(MathOperation.EUCLIDEAN_DISTANCE.a(new Double[] { Double.valueOf(MathOperation.WRAPPED_TO_180_DISTANCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZI.get(1)).ik()), Double.valueOf(RotationUtil.c(new Vector3d(((RotationSample)RotationSamples.ZI.get(1)).ih(), 0.0, ((RotationSample)RotationSamples.ZI.get(1)).ij()), new Vector3d(((RotationSample)RotationSamples.ZJ.jq()).ih(), 0.0, ((RotationSample)RotationSamples.ZJ.jq()).ij())).getX()) })), Double.valueOf(MathOperation.DIFFERENCE.a(new Double[] { Double.valueOf(((RotationSample)RotationSamples.ZI.get(1)).il()), Double.valueOf(RotationUtil.c(new Vector3d(((RotationSample)RotationSamples.ZI.get(1)).ih(), ((RotationSample)RotationSamples.ZI.get(1)).ii(), ((RotationSample)RotationSamples.ZI.get(1)).ij()), new Vector3d(((RotationSample)RotationSamples.ZJ.jq()).ih(), ((RotationSample)RotationSamples.ZJ.jq()).ii(), ((RotationSample)RotationSamples.ZJ.jq()).ij())).getY()) })) })) }))),
    ENEMY_CONTRIBUTION_TO_DELTA_ROTATION_DISTANCE_FROM_PERFECT_ROTATIONS(() -> Double.valueOf((double)RotationMetric.PLAYER_CONTRIBUTION_TO_DELTA_ROTATION_DISTANCE_FROM_PERFECT_ROTATIONS.t(true) * -1.0)),
    PLAYER_CLICKED(() -> Double.valueOf(((RotationSample)RotationSamples.ZI.jq()).im() ? 1 : 0)),
    PLAYER_LAST_CLICK(() -> Double.valueOf(Integer.valueOf(RotationSamples.ZI.stream().filter(su -> su.im()).findFirst().map(su2 -> Integer.valueOf(RotationSamples.ZI.size() - RotationSamples.ZI.indexOf((Object)su2) - 1)).orElse(Integer.valueOf(RotationSamples.ZI.rS())))));

    private final Supplier<Double> aaQ;
    private static final RotationMetric[] $VALUES;

    private RotationMetric(final Supplier<Double> aaQ) {
        this.aaQ = aaQ;
    }

    public Double io() {
        return Double.valueOf(this.aaQ.get());
    }

    public Double C(final int n) {
        return this.d(n, false);
    }

    public Double t(final boolean b) {
        return this.d(0, b);
    }

    public Double d(final int n, final boolean b) {
        final IndexedEvictingList jr = RotationSamples.ZI.jr();
        final IndexedEvictingList jr2 = RotationSamples.ZJ.jr();
        if (b) {
            RotationSamples.ZI = jr2;
            RotationSamples.ZJ = jr;
        }
        for (int i = 0; i < n; ++i) {
            RotationSamples.ZI.removeLast();
            RotationSamples.ZJ.removeLast();
        }
        final Double n2 = Double.valueOf(this.aaQ.get());
        RotationSamples.ZI = jr;
        RotationSamples.ZJ = jr2;
        return n2;
    }

    private static RotationMetric[] jp() {
        return new RotationMetric[] { RotationMetric.ID, RotationMetric.PLAYER_X, RotationMetric.PLAYER_Y, RotationMetric.PLAYER_Z, RotationMetric.PLAYER_YAW, RotationMetric.PLAYER_PITCH, RotationMetric.TARGET_X, RotationMetric.TARGET_Y, RotationMetric.TARGET_Z, RotationMetric.TARGET_YAW, RotationMetric.TARGET_PITCH, RotationMetric.DIFFERENCE_X, RotationMetric.DIFFERENCE_Y, RotationMetric.DIFFERENCE_Z, RotationMetric.PLAYER_PERFECT_YAW, RotationMetric.PLAYER_PERFECT_PITCH, RotationMetric.PLAYER_DELTA_X, RotationMetric.PLAYER_DELTA_Y, RotationMetric.PLAYER_DELTA_Z, RotationMetric.PLAYER_DELTA_YAW, RotationMetric.PLAYER_DELTA_PITCH, RotationMetric.PLAYER_YAW_DIFFERENCE_FROM_PERFECT_YAW, RotationMetric.ENEMY_YAW_DIFFERENCE_FROM_PERFECT_YAW, RotationMetric.PLAYER_PITCH_DIFFERENCE_FROM_PERFECT_PITCH, RotationMetric.ENEMY_PITCH_DIFFERENCE_FROM_PERFECT_PITCH, RotationMetric.HORIZONTAL_DISTANCE, RotationMetric.VERTICAL_DISTANCE, RotationMetric.PLAYER_CONTRIBUTION_TO_DELTA_HORIZONTAL_DISTANCE, RotationMetric.ENEMY_CONTRIBUTION_TO_DELTA_HORIZONTAL_DISTANCE, RotationMetric.PLAYER_CONTRIBUTION_TO_DELTA_VERTICAL_DISTANCE, RotationMetric.ENEMY_CONTRIBUTION_TO_DELTA_VERTICAL_DISTANCE, RotationMetric.ANGLE_OF_CROSS_HAIR_TO_ENEMY, RotationMetric.DELTA_HORIZONTAL_ANGLE_TO_ENEMY, RotationMetric.DELTA_VERTICAL_ANGLE_TO_ENEMY, RotationMetric.PLAYER_HORIZONTAL_DELTA, RotationMetric.PLAYER_HORIZONTAL_MOVEMENT_ANGLE, RotationMetric.PLAYER_FORWARD_PERCENTAGE, RotationMetric.PLAYER_FORWARD_DELTA, RotationMetric.PLAYER_LATERAL_DELTA, RotationMetric.ENEMY_FORWARD_DELTA, RotationMetric.ENEMY_LATERAL_DELTA, RotationMetric.PLAYER_CONTRIBUTION_TO_DELTA_HORIZONTAL_ANGLE_TO_ENEMY, RotationMetric.ENEMY_CONTRIBUTION_TO_DELTA_HORIZONTAL_ANGLE_TO_PLAYER, RotationMetric.PLAYER_CONTRIBUTION_TO_DELTA_VERTICAL_ANGLE_TO_ENEMY, RotationMetric.ENEMY_CONTRIBUTION_TO_DELTA_VERTICAL_ANGLE_TO_PLAYER, RotationMetric.PLAYER_DELTA_ROTATION, RotationMetric.PLAYER_ROTATION_DISTANCE_FROM_PERFECT_ROTATIONS, RotationMetric.PLAYER_DELTA_ROTATION_DISTANCE_FROM_PERFECT_ROTATIONS, RotationMetric.PLAYER_CONTRIBUTION_TO_DELTA_ROTATION_DISTANCE_FROM_PERFECT_ROTATIONS, RotationMetric.ENEMY_CONTRIBUTION_TO_DELTA_ROTATION_DISTANCE_FROM_PERFECT_ROTATIONS, RotationMetric.PLAYER_CLICKED, RotationMetric.PLAYER_LAST_CLICK };
    }

    static {
        $VALUES = jp();
    }
}
