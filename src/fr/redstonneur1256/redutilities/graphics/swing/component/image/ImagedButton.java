package fr.redstonneur1256.redutilities.graphics.swing.component.image;

import fr.redstonneur1256.redutilities.graphics.ImageHelper;
import fr.redstonneur1256.redutilities.graphics.swing.component.abstractComp.AbstractButton;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class ImagedButton extends AbstractButton {

    private BufferedImage image;
    private BufferedImage hoverImage;
    private BufferedImage disabledImage;

    public ImagedButton(BufferedImage image) {
        this(image, null, null);
    }

    public ImagedButton(BufferedImage image, BufferedImage hoverImage) {
        this(image, hoverImage, null);
    }

    public ImagedButton(BufferedImage image, BufferedImage hoverImage, BufferedImage disabledImage) {
        Objects.requireNonNull(image, "Image cannot be null");
        this.image = image;
        this.hoverImage = hoverImage == null ? ImageHelper.color(image, new Color(255, 255, 255, 127)) : hoverImage;
        this.disabledImage = disabledImage == null ? ImageHelper.convertBlackAndWhite(image) : disabledImage;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        BufferedImage image = isEnabled() ? (isHovering() ? hoverImage : this.image) : disabledImage;

        graphics.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        Objects.requireNonNull(image, "image cannot be null");
        this.image = image;
    }

    public BufferedImage getHoverImage() {
        return hoverImage;
    }

    public void setHoverImage(BufferedImage hoverImage) {
        Objects.requireNonNull(image, "hoverImage cannot be null");
        this.hoverImage = hoverImage;
    }

    public BufferedImage getDisabledImage() {
        return disabledImage;
    }

    public void setDisabledImage(BufferedImage disabledImage) {
        Objects.requireNonNull(image, "disabledImage cannot be null");
        this.disabledImage = disabledImage;
    }

}
