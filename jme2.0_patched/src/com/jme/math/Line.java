/*
 * Copyright (c) 2003-2009 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jme.math;

import java.io.IOException;
import java.io.Serializable;
import java.nio.FloatBuffer;

import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import com.jme.util.geom.BufferUtils;

/**
 * <p>
 * <code>Line</code> defines a line. The line is defined as infinite based 
 * on an origin and a direction.
 * </p>
 * <p>
 * Direction needs to be normalized at any time (otherwise, the distance()
 * method is known to fail and other problems may exist).
 * </p>
 * 
 * @author Mark Powell
 * @author Joshua Slack
 */
public class Line implements Serializable, Savable, Cloneable {

    // TODO: merge with Ray?

    private static final long serialVersionUID = 1L;

    /**
     * Line origin (may be any point in the line).
     */
    private Vector3f origin;

    /**
     * Line direction.
     */
    private Vector3f direction;

    private static final Vector3f compVec1 = new Vector3f();
    private static final Vector3f compVec2 = new Vector3f();
    private static final Matrix3f compMat1 = new Matrix3f();
    private static final Eigen3f compEigen1 = new Eigen3f();

    /**
     * Constructor instantiates a new <code>Line</code> object. The origin and
     * direction are set to defaults (0,0,0). Note this is an invalid state, as
     * direction needs to be normalized.
     * 
     * @see Line
     */
    public Line() {
        origin = new Vector3f();
        direction = new Vector3f();
    }

    /**
     * Constructor instantiates a new <code>Line</code> object. The origin and
     * direction are set via the parameters. Direction needs to be normalized.
     * 
     * @param origin
     *            the origin of the line.
     * @param direction
     *            the direction of the line.
     */
    public Line(Vector3f origin, Vector3f direction) {
        this.origin = origin;
        this.direction = direction;
    }

    /**
     * <code>getOrigin</code> returns the origin of the line.
     * @return the origin of the line.
     */
    public Vector3f getOrigin() {
        return origin;
    }

    /**
     * Sets the origin of the line.
     * <code>setOrigin</code> sets the origin of the line.
     * 
     * @param origin
     *            the origin of the line.
     */
    public void setOrigin(Vector3f origin) {
        this.origin = origin;
    }

    /**
     * Gets the direction of the line.
     * <code>getDirection</code> returns the direction of the line.
     * 
     * @return the direction of the line.
     */
    public Vector3f getDirection() {
        return direction;
    }

    /**
     * <code>setDirection</code> sets the direction of the line.
     * 
     * @param direction
     *            the direction of the line.
     */
    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    /**
     * <p>
     * Returns the square of the distance from the given point to the closest
     * point of this line.
     * </p>
     * <p>
     * This method is faster than {@link Line#distance(Vector3f)}, so it's a
     * good alternative when the absolute distance is not needed.
     * </p>
     * 
     * @param point
     *            the point to calculate the distance to this line
     * @return the distance squared from the given point to this line
     */
    public float distanceSquared(Vector3f point) {
        point.subtract(origin, compVec1);
        float lineParameter = direction.dot(compVec1);
        origin.add(direction.mult(lineParameter, compVec2), compVec2);
        compVec2.subtract(point, compVec1);
        return compVec1.lengthSquared();
    }

    /**
     * <p>
     * Returns the distance from the given point to the closest point of this
     * line.
     * </p>
     * <p>
     * When the exact distance is not needed the faster method
     * {@link Line#distanceSquared(Vector3f)} can be used instead (for example
     * if comparing or sorting several distances, where using the squared
     * value is equivalent).
     * </p>
     * 
     * @param point
     *            the point to calculate the distance to this line
     * @return the distance from the given point to this line
     */
    public float distance(Vector3f point) {
        return FastMath.sqrt(distanceSquared(point));
    }

    
    /**
     * @param points
     */
    public void orthogonalLineFit(FloatBuffer points) {
        if (points == null) {
            return;
        }

        points.rewind();

        // compute average of points
        int length = points.remaining() / 3;

        BufferUtils.populateFromBuffer(origin, points, 0);
        for (int i = 1; i < length; i++) {
            BufferUtils.populateFromBuffer(compVec1, points, i);
            origin.addLocal(compVec1);
        }

        origin.multLocal(1f / (float) length);

        // compute sums of products
        float sumXX = 0.0f, sumXY = 0.0f, sumXZ = 0.0f;
        float sumYY = 0.0f, sumYZ = 0.0f, sumZZ = 0.0f;

        points.rewind();
        for (int i = 0; i < length; i++) {
            BufferUtils.populateFromBuffer(compVec1, points, i);
            compVec1.subtract(origin, compVec2);
            sumXX += compVec2.x * compVec2.x;
            sumXY += compVec2.x * compVec2.y;
            sumXZ += compVec2.x * compVec2.z;
            sumYY += compVec2.y * compVec2.y;
            sumYZ += compVec2.y * compVec2.z;
            sumZZ += compVec2.z * compVec2.z;
        }

        // find the smallest eigen vector for the direction vector
        compMat1.m00 = sumYY + sumZZ;
        compMat1.m01 = -sumXY;
        compMat1.m02 = -sumXZ;
        compMat1.m10 = -sumXY;
        compMat1.m11 = sumXX + sumZZ;
        compMat1.m12 = -sumYZ;
        compMat1.m20 = -sumXZ;
        compMat1.m21 = -sumYZ;
        compMat1.m22 = sumXX + sumYY;

        compEigen1.calculateEigen(compMat1);
        direction = compEigen1.getEigenVector(0);
    }

    /**
     * <p><code>random</code> determines a random point along the line.</p>
     * <p>Note: this implementation may be broken. Please check source code
     * before using.</p>
     * @return a random point on the line.
     */
    public Vector3f random() {
        return random(null);
    }

    /**
     * <p><code>random</code> determines a random point along the line.</p>
     * <p>Note: this implementation may be broken. Please check source code
     * before using.</p>
     * <p>If the passed vector is null, a vector will be created to store
     * the result of the operation.</p> 
     * @param result Vector to store result in. If null, a new vector will be created and returned.
     * @return a random point on the line.
     */
    public Vector3f random(Vector3f result) {
        if (result == null) {
            result = new Vector3f();
        }
        float rand = (float) Math.random();

        result.x = (origin.x * (1 - rand)) + (direction.x * rand);
        result.y = (origin.y * (1 - rand)) + (direction.y * rand);
        result.z = (origin.z * (1 - rand)) + (direction.z * rand);

        return result;
    }

    /**
     * Support for JME exporting.
     * @param e The JME exporter to write to.
     * @see com.jme.util.export.Savable#write(com.jme.util.export.JMEExporter)
     */
    public void write(JMEExporter e) throws IOException {
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(origin, "origin", Vector3f.ZERO);
        capsule.write(direction, "direction", Vector3f.ZERO);
    }

    /**
     * Support for JME importing.
     * @param e The JME importer to read from.
     * @see com.jme.util.export.Savable#read(com.jme.util.export.JMEImporter)
     */
    public void read(JMEImporter e) throws IOException {
        InputCapsule capsule = e.getCapsule(this);
        origin = (Vector3f) capsule.readSavable("origin", Vector3f.ZERO.clone());
        direction = (Vector3f) capsule.readSavable("direction", Vector3f.ZERO.clone());
    }

    /**
     * Returns class type (for exporting/importing purposes).
     * @return Class tag.
     */
    public Class<? extends Line> getClassTag() {
        return this.getClass();
    }

    /**
     * Provides deep-copy object clonation.
     * @return A cloned line, with its own copy of direction and origin vectors.
     * @see java.lang.Object#clone()
     */
    @Override
    public Line clone() {
        try {
            Line line = (Line) super.clone();
            line.direction = direction.clone();
            line.origin = origin.clone();
            return line;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    
}
