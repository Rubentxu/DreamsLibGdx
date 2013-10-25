package com.rubentxu.juegos.core.varios;


import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class SpriteTest {

    private Texture texture128x512;

    @Before
    public void setup() {
        texture128x512 = org.easymock.EasyMock.createMock(Texture.class);
        expect(texture128x512.getWidth()).andReturn(128).anyTimes();
        expect(texture128x512.getHeight()).andReturn(512).anyTimes();
        replay(texture128x512);
    }

    @Test
    public void shouldReturnBoundingRectangleWithSamePositionAndWidth() {
        Sprite sprite = new Sprite(new TextureRegion(texture128x512, 0, 0, 128, 512));
        sprite.setPosition(50f, 70f);
        sprite.setOrigin(65f, 85f);
        assertThat(sprite.getX(), IsEqual.equalTo(50f));
        assertThat(sprite.getY(), IsEqual.equalTo(70f));
        assertThat(sprite.getOriginX(), IsEqual.equalTo(65f));
        assertThat(sprite.getOriginY(), IsEqual.equalTo(85f));

    }

    @Test
    public void shouldReturnSamePositionWhenFlippedHorizontally() {
        Sprite sprite = new Sprite(new TextureRegion(texture128x512, 0, 0, 128, 512));
        sprite.setPosition(50f, 70f);
        sprite.setOrigin(65f, 85f);
        sprite.flip(true, false);
        assertThat(sprite.getX(), IsEqual.equalTo(50f));
        assertThat(sprite.getY(), IsEqual.equalTo(70f));
        assertThat(sprite.getOriginX(), IsEqual.equalTo(65f));
        assertThat(sprite.getOriginY(), IsEqual.equalTo(85f));
    }


    @Test
    public void shouldReturnSamePositionWhenFlippedVertically() {
        Sprite sprite = new Sprite(new TextureRegion(texture128x512, 0, 0, 128, 512));
        sprite.setPosition(50f, 70f);
        sprite.setOrigin(65f, 85f);
        sprite.flip(false, true);
        assertThat(sprite.getX(), IsEqual.equalTo(50f));
        assertThat(sprite.getY(), IsEqual.equalTo(70f));
        assertThat(sprite.getOriginX(), IsEqual.equalTo(65f));
        assertThat(sprite.getOriginY(), IsEqual.equalTo(85f));
    }
}