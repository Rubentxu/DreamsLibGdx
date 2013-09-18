/**
 * Copyright 2013 Robin Stumm (serverkorken@googlemail.com, http://dermetfan.bplaced.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rubentxu.juegos.core.utils.dermetfan.math;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * contains some useful methods for geometric calculations
 * @author dermetfan
 */
public abstract class GeometryUtils {

    /** a temporary variable used by some methods
     *  warning not safe to use as it may change any time */
    public static Vector2 tmpVec = new Vector2();

    /** @see #tmpVec */
    public static Vector2[] tmpVecArr;

    /** @see #tmpVec */
    public static float[] tmpFloatArr;

    /** @return a Vector2 representing the size of a rectangle containing all given vertices */
    public static Vector2 size(Vector2[] vertices, Vector2 output) {
        return output.set(amplitude(filterX(vertices)), amplitude(filterY(vertices)));
    }

    /** @see #size(Vector2[], Vector2) */
    public static Vector2 size(Vector2[] vertices) {
        return size(vertices, tmpVec);
    }

    /** @return the peak-to-peak amplitude of the given array */
    public static float amplitude(float[] f) {
        return Math.abs(max(f) - min(f));
    }

    /** @return the largest element of the given array */
    public static float max(float[] floats) {
        float max = Float.NEGATIVE_INFINITY;
        for(float f : floats)
            max = f > max ? f : max;
        return max;
    }

    /** @return the smallest element of the given array */
    public static float min(float[] floats) {
        float min = Float.POSITIVE_INFINITY;
        for(float f : floats)
            min = f < min ? f : min;
        return min;
    }

    /** @return the x values of the given vertices */
    public static float[] filterX(Vector2[] vertices, float[] output) {
        output = new float[vertices.length];
        for(int i = 0; i < output.length; i++)
            output[i] = vertices[i].x;
        return output;
    }

    /** @see #filterX(Vector2[], float[]) */
    public static float[] filterX(Vector2[] vertices) {
        return filterX(vertices, tmpFloatArr);
    }

    /** @return the y values of the given vertices */
    public static float[] filterY(Vector2[] vertices, float[] output) {
        output = new float[vertices.length];
        for(int i = 0; i < output.length; i++)
            output[i] = vertices[i].y;
        return output;
    }

    /** @see #filterY(Vector2[], float[]) */
    public static float[] filterY(Vector2[] vertices) {
        return filterY(vertices, tmpFloatArr);
    }

    /**
     * scales the given float array to have the given min and max values
     * @param values the values to scale
     * @param min the desired minimal value in the array
     * @param max the desired maximal value in the array
     * @return the rescaled array
     */
    public static float[] scale(float[] values, float min, float max) {
        float tmp = amplitude(values) / (max - min);
        for(int i = 0; i < values.length; i++)
            values[i] /= tmp;

        tmp = min - min(values);
        for(int i = 0; i < values.length; i++)
            values[i] += tmp;

        return values;
    }

    /**
     * @param vector2s the Vector2[] to convert to a float[]
     * @return the float[] converted from the given Vector2[]
     */
    public static float[] toFloatArray(Vector2[] vector2s, float[] output) {
        output = new float[vector2s.length * 2];

        int vi = -1;
        for(int i = 0; i < output.length; i++)
            if(i % 2 == 0)
                output[i] = vector2s[++vi].x;
            else
                output[i] = vector2s[vi].y;

        return output;
    }

    /** @see #toFloatArray(Vector2[], float[]) */
    public static float[] toFloatArray(Vector2[] vector2s) {
        return toFloatArray(vector2s, tmpFloatArr);
    }

    /**
     * @param floats the float[] to convert to a Vector2[]
     * @return the Vector2[] converted from the given float[]
     */
    public static Vector2[] toVector2Array(float[] floats, Vector2[] output) {
        if(floats.length % 2 != 0)
            throw new IllegalArgumentException("the float array's length is not dividable by two, so it won't make up a Vector2 array: " + floats.length);

        output = new Vector2[floats.length / 2];

        int fi = -1;
        for(int i = 0; i < output.length; i++)
            output[i] = new Vector2(floats[++fi], floats[++fi]);

        return output;
    }

    /** @see #toVector2Array(float[], Vector2[]) */
    public static Vector2[] toVector2Array(float[] floats) {
        return toVector2Array(floats, tmpVecArr);
    }

    public static Vector2[] toVector2Array(short[] shorts) {
        float[] floats = new float[shorts.length];
        for (int i = 0 ; i < shorts.length; i++)
        {
            floats[i] = (float) shorts[i];
        }
        return toVector2Array(floats, tmpVecArr);
    }

    /**
     * @param vertexCount the number of vertices for each {@link Polygon}
     * @see #toPolygonArray(Vector2[], int[])
     */
    public static Polygon[] toPolygonArray(Vector2[] vertices, int vertexCount) {
        int[] vertexCounts = new int[vertices.length / vertexCount];
        for(int i = 0; i < vertexCounts.length; i++)
            vertexCounts[i] = vertexCount;
        return toPolygonArray(vertices, vertexCounts);
    }

    /**
     * @param vertices the vertices which should be split into a {@link Polygon} array
     * @param vertexCounts the number of vertices of each {@link Polygon}
     * @return the {@link Polygon} array extracted from the vertices
     */
    public static Polygon[] toPolygonArray(Vector2[] vertices, int[] vertexCounts) {
        Polygon[] polygons = new Polygon[vertexCounts.length];

        int vertice = -1;
        for(int i = 0; i < polygons.length; i++) {
            tmpVecArr = new Vector2[vertexCounts[i]];
            for(int i2 = 0; i2 < tmpVecArr.length; i2++)
                tmpVecArr[i2] = vertices[++vertice];
            polygons[i] = new Polygon(toFloatArray(tmpVecArr));
        }

        return polygons;
    }

    /** @see Polygon#area() */
    public static float area(float[] vertices) {
        // from com.badlogic.gdx.math.Polygon#area()
        float area = 0;

        int x1, y1, x2, y2;
        for(int i = 0; i < vertices.length; i += 2) {
            x1 = i;
            y1 = i + 1;
            x2 = (i + 2) % vertices.length;
            y2 = (i + 3) % vertices.length;

            area += vertices[x1] * vertices[y2];
            area -= vertices[x2] * vertices[y1];
        }

        return area /= 2;
    }

    /**
     * @param polygon the polygon, assumed to be simple
     * @return if the vertices are in clockwise order
     */
    public static boolean areVerticesClockwise(Polygon polygon) {
        return polygon.area() < 0;
    }

    /** @see #areVerticesClockwise(Polygon) */
    public static boolean areVerticesClockwise(float[] vertices) {
        if(vertices.length <= 4)
            return true;
        return area(vertices) < 0;
    }

    /** @see #isConvex(Vector2[]) */
    public static boolean isConvex(float[] vertices) {
        return isConvex(toVector2Array(vertices));
    }

    /** @see #isConvex(Vector2[]) */
    public static boolean isConvex(Polygon polygon) {
        return isConvex(polygon.getVertices());
    }

    /**
     * @param vertices the vertices of the polygon to examine for convexity
     * @return if the polygon is convex
     */
    public static boolean isConvex(Vector2[] vertices) {
        // http://www.sunshine2k.de/coding/java/Polygon/Convex/polygon.htm

        Vector2 p, v, u;
        float res = 0;
        for(int i = 0; i < vertices.length; i++) {
            p = vertices[i];
            tmpVec = vertices[(i + 1) % vertices.length];
            v = new Vector2();
            v.x = tmpVec.x - p.x;
            v.y = tmpVec.y - p.y;
            u = vertices[(i + 2) % vertices.length];

            if(i == 0) // in first loop direction is unknown, so save it in res
                res = u.x * v.y - u.y * v.x + v.x * p.y - v.y * p.x;
            else {
                float newres = u.x * v.y - u.y * v.x + v.x * p.y - v.y * p.x;
                if(newres > 0 && res < 0 || newres < 0 && res > 0)
                    return false;
            }
        }

        return true;
    }

    /**
     * converts point to its coordinates on an isometric grid
     * @param point the point to convert
     * @param cellWidth the width of the grid cells
     * @param cellHeight the height of the grid cells
     * @return the given point converted to its coordinates on an isometric grid
     */
    public static Vector2 toIsometricGridPoint(Vector2 point, float cellWidth, float cellHeight) {
        point.x = (point.x /= cellWidth) - ((point.y = (point.y - cellHeight / 2) / cellHeight + point.x) - point.x);
        return point;
    }

    /** @see #toIsometricGridPoint(Vector2, float, float) */
    public static Vector3 toIsometricGridPoint(Vector3 point, float cellWidth, float cellHeight) {
        point.x = (point.x /= cellWidth) - ((point.y = (point.y - cellHeight / 2) / cellHeight + point.x) - point.x);
        return point;
    }

}