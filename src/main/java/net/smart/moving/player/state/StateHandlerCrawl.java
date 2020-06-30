package net.smart.moving.player.state;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.smart.moving.player.PlayerConstants;
import net.smart.moving.player.SmartBase;

public class StateHandlerCrawl extends StateHandler {

    private void applyHorizontalDamping(EntityPlayer player, float horizontalDamping) {
        player.motionX *= horizontalDamping;
        player.motionZ *= horizontalDamping;
    }

    public void afterOnUpdate(EntityPlayer player) {
        applyHorizontalDamping(player, PlayerConstants.CRAWL_DAMPING_FACTOR);
        player.height = PlayerConstants.CRAWL_HEIGHT;
        player.eyeHeight = PlayerConstants.CRAWL_EYE_HEIGHT;
    }

    public void updatePlayerRotations(EntityPlayer player, SmartBase smartBase) {
        double diffPosX = player.posX - player.prevPosX;
        double diffPosZ = player.posZ - player.prevPosZ;
        float horizontalMove = (float) Math.sqrt(diffPosX * diffPosX + diffPosZ * diffPosZ);
        float yawOffset = player.rotationYaw;

        if (horizontalMove > 0) {
            float moveDir = (float) (MathHelper.atan2(diffPosZ, diffPosX) * 180F / Math.PI - 90.0F);
            float yawMoveDiff = MathHelper.abs(MathHelper.wrapDegrees(player.rotationYaw) - moveDir);

            yawOffset = moveDir;
        }

        if (player.swingProgress > 0.0F)
            yawOffset = player.rotationYaw;

        float f = MathHelper.wrapDegrees(yawOffset - player.renderYawOffset);
        player.renderYawOffset += f * 0.3F;
        float f1 = MathHelper.wrapDegrees(player.rotationYaw - player.renderYawOffset);

        if (f1 < -75.0F)
            f1 = -75.0F;

        if (f1 >= 75.0F)
            f1 = 75.0F;

        player.renderYawOffset = player.rotationYaw - f1;

        if (f1 * f1 > 2500.0F)
            player.renderYawOffset += f1 * 0.2F;
    }
}
