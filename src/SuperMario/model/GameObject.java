package SuperMario.model;

import SuperMario.config.GameConstants;
import SuperMario.logic.collision.CollisionBounds;
import SuperMario.logic.physics.Physics;
import SuperMario.logic.render.EntityRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {

    private double x, y;

    private double velX, velY;

    private Dimension dimension;

    private BufferedImage style;

    private boolean toRight;

    private double gravityAcc;

    private boolean falling, jumping;

    public GameObject(double x, double y, BufferedImage style) {
        setLocation(x, y);
        setStyle(style);

        if (style != null) {
            setDimension(style.getWidth(), style.getHeight());
        }

        setVelX(0);
        setVelY(0);
        setGravityAcc(GameConstants.GRAVITY);
        jumping = false;
        falling = true;
    }

    public void draw(Graphics g) {
        EntityRenderer.drawSprite(this, g);
    }

    public void updateLocation() {
        Physics.updateLocation(this);
    }

    public void setLocation(double x, double y) {
        setX(x);
        setY(y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setDimension(int width, int height) {
        this.dimension = new Dimension(width, height);
    }

    public BufferedImage getStyle() {
        return style;
    }

    public void setStyle(BufferedImage style) {
        this.style = style;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public double getGravityAcc() {
        return gravityAcc;
    }

    public void setGravityAcc(double gravityAcc) {
        this.gravityAcc = gravityAcc;
    }

    public Rectangle getTopBounds() {
        return CollisionBounds.getTopBounds(this);
    }

    public Rectangle getBottomBounds() {
        return CollisionBounds.getBottomBounds(this);
    }

    public Rectangle getLeftBounds() {
        return CollisionBounds.getLeftBounds(this);
    }

    public Rectangle getRightBounds() {
        return CollisionBounds.getRightBounds(this);
    }

    public Rectangle getBounds() {
        return CollisionBounds.getBounds(this);
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public void setToRight(boolean toRight) {
        this.toRight = toRight;
    }

    public boolean isToRight() {
        return toRight;
    }
}
