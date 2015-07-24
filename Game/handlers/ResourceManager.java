package com.devour.all.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;

/**
 * Created by Jacob on 7/23/2015.
 */
public class ResourceManager {

    private HashMap<String, Texture> textures;
    private HashMap<String, TextureAtlas> textureAtlases;
    private HashMap<String, Music> music;
    private HashMap<String, Sound> sounds;

    public ResourceManager(){
        textures = new HashMap<String, Texture>();
        textureAtlases = new HashMap<String, TextureAtlas>();
        music = new HashMap<String, Music>();
        sounds = new HashMap<String, Sound>();
    }


    /*
    * TEXTURES
     */
    public void loadTexture(String path, String key){
        Texture texture = new Texture(Gdx.files.internal(path));
        textures.put(key, texture);
    }

    public Texture getTexture(String key){
        return textures.get(key);
    }

    public void disposeTexture(String key){
        Texture texture = textures.get(key);
        if(texture != null) texture.dispose();
    }

    /*
    * TEXTUREATLASES
     */
    public void loadTextureAtlas(String path, String key){
        TextureAtlas texture = new TextureAtlas(Gdx.files.internal(path));
        textureAtlases.put(key, texture);
    }

    public TextureAtlas getTextureAtlas(String key){
        return textureAtlases.get(key);
    }

    public void disposeTextureAtlas(String key){
        TextureAtlas textureAtlas = textureAtlases.get(key);
        if(textureAtlas != null) textureAtlas.dispose();
    }

    /*
    * MUSIC
     */
    public void loadMusic(String path, String key) {
        Music m = Gdx.audio.newMusic(Gdx.files.internal(path));
        music.put(key, m);
    }
    public Music getMusic(String key) {
        return music.get(key);
    }
    public void removeMusic(String key) {
        Music m = music.get(key);
        if(m != null) {
            music.remove(key);
            m.dispose();
        }
    }

    /*
    * SOUNDS
     */
    public void loadSound(String path, String key) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        sounds.put(key, sound);
    }
    public Sound getSound(String key) {
        return sounds.get(key);
    }
    public void removeSound(String key) {
        Sound sound = sounds.get(key);
        if(sound != null) {
            sounds.remove(key);
            sound.dispose();
        }
    }
}
