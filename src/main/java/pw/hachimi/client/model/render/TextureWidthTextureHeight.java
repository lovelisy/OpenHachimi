package pw.hachimi.client.model.render;

import pw.hachimi.client.render.IdentifierNativeImageBackedTextureManager;

public record TextureWidthTextureHeight(int textureWidth, int textureHeight, int width, int height, char value, IdentifierNativeImageBackedTextureManager owner) {
}
