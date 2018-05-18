package com.gdx.game.model.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class PlainModel extends EntityModel {

    private float width;
    private float heigth;
    private float depth;

    private static final Material material = new Material(ColorAttribute.createDiffuse(Color.VIOLET));

    public PlainModel(int id) {
        this(id, 0, 0, 0, 4, 1, 12);
    }

    public PlainModel(int id, float x, float y, float z, float w, float h, float d) {

        super(x, y, z);

        this.width = w;
        this.heigth = h;
        this.depth = d;

        setModel(buildModel(id));
    }

    private Model buildModel(int id) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = String.format("plain%d", id);
        modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material)
                .box(this.width, this.heigth, this.depth);
        return modelBuilder.end();
        //return modelBuilder.createBox(this.width, this.heigth, this.depth, GL20.GL_TRIANGLES, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }

    public float getWidth() {
        return width;
    }

    public float getHeigth() {
        return heigth;
    }

    public float getDepth() {
        return depth;
    }

    public void setWidth(int id, float width) {
        this.width = width;
        setModel(buildModel(id));
    }

    public void setHeigth(int id, float heigth) {
        this.heigth = heigth;
        setModel(buildModel(id));
    }

    public void setDepth(int id, float depth) {
        this.depth = depth;
        setModel(buildModel(id));
    }

}