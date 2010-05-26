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

package com.jme.bounding;

import java.io.IOException;
import java.nio.FloatBuffer;

import com.jme.intersection.IntersectionRecord;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Plane;
import com.jme.math.Quaternion;
import com.jme.math.Ray;
import com.jme.math.Triangle;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.math.Plane.Side;
import com.jme.scene.TriMesh;
import com.jme.system.DisplaySystem;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.geom.BufferUtils;

/**
 * Started Date: Sep 5, 2004 <br>
 * <br>
 * 
 * @author Jack Lindamood
 * @author Joshua Slack (alterations for .9)
 * @version $Id: jme2_r4452.patch,v 1.1 2010/04/13 15:10:53 dcs0ah1 Exp $+ */

public class OrthogonalBoundingBox extends BoundingVolume {
    private static final long serialVersionUID = 1L;
    // TODO: Really need to move from static computefields to an object pool.

    static private final Vector3f _compVect4 = new Vector3f();

    static private final Vector3f _compVect5 = new Vector3f();

    static private final Vector3f _compVect6 = new Vector3f();

    static private final Vector3f _compVect7 = new Vector3f();

    static private final Vector3f tempVe = new Vector3f();

    static private final Matrix3f tempMa = new Matrix3f();

    static private final Quaternion tempQa = new Quaternion();

    static private final Quaternion tempQb = new Quaternion();

    private static final float[] fWdU = new float[3];

    private static final float[] fAWdU = new float[3];

    private static final float[] fDdU = new float[3];

    private static final float[] fADdU = new float[3];

    private static final float[] fAWxDdU = new float[3];

    private static final float[] tempFa = new float[3];

    private static final float[] tempFb = new float[3];

    /** X axis of the Oriented Box. */
    public final Vector3f xAxis = new Vector3f(1, 0, 0);

    /** Y axis of the Oriented Box. */
    public final Vector3f yAxis = new Vector3f(0, 1, 0);

    /** Z axis of the Oriented Box. */
    public final Vector3f zAxis = new Vector3f(0, 0, 1);

    /** Extents of the box along the x,y,z axis. */
    public final Vector3f extent = new Vector3f(0, 0, 0);

    /** Vector array used to store the array of 8 corners the box has. */
    public final Vector3f[] vectorStore = new Vector3f[4];

	private static final Plane XYPlane = new Plane(new Vector3f(0,0,1),0);

    public boolean correctCorners = false;

    public OrthogonalBoundingBox() {
        for (int x = 0; x < 4; x++)
            vectorStore[x] = new Vector3f();
    }

    public Type getType() {
        return Type.OrthogonalBoundingBox;
    }

    public BoundingVolume transform(Quaternion rotate, Vector3f translate,
            Vector3f scale, BoundingVolume store) {
        rotate.toRotationMatrix(tempMa);
        return transform(tempMa, translate, scale, store);
    }

    public BoundingVolume transform(Matrix3f rotate, Vector3f translate,
            Vector3f scale, BoundingVolume store) {
        if (store == null || store.getType() != Type.OrthogonalBoundingBox) {
            store = new OrthogonalBoundingBox();
        }
        OrthogonalBoundingBox toReturn = (OrthogonalBoundingBox) store;
        toReturn.extent.set(FastMath.abs(extent.x * scale.x), 
                FastMath.abs(extent.y * scale.y), 
                0);
        rotate.mult(xAxis, toReturn.xAxis);
        rotate.mult(yAxis, toReturn.yAxis);
        rotate.mult(zAxis, toReturn.zAxis);
        center.mult(scale, toReturn.center);
        rotate.mult(toReturn.center, toReturn.center);
        toReturn.center.addLocal(translate);
        toReturn.correctCorners = false;
        return toReturn;
    }

    public Side whichSide(Plane plane) {
        float fRadius = FastMath.abs(extent.x * (plane.getNormal().dot(xAxis)))
        + FastMath.abs(extent.y * (plane.getNormal().dot(yAxis)));
        _compVect1.set(DisplaySystem.getDisplaySystem().getWorldCoordinates(new Vector2f(center.x,center.y), 0f));
        _compVect1.setZ(0);
        float fDistance = plane.pseudoDistance(_compVect1);
        if (fDistance <= -fRadius)
        	return Side.NEGATIVE;
        else if (fDistance >= fRadius)
        	return Side.POSITIVE;
        else
        	return Side.NONE;
    }

    public void computeFromPoints(FloatBuffer points) {
        containAABB(points);
    }

    /**
     * Calculates an AABB of the given point values for this OBB.
     * 
     * @param points
     *            The points this OBB should contain.
     */
    private void containAABB(FloatBuffer points) {
        if (points == null || points.limit() <= 2) { // we need at least a 3
            // float vector
            return;
        }

        BufferUtils.populateFromBuffer(_compVect1, points, 0);
        float minX = _compVect1.x, minY = _compVect1.y;
        float maxX = _compVect1.x, maxY = _compVect1.y;

        for (int i = 1, len = points.limit() / 3; i < len; i++) {
            BufferUtils.populateFromBuffer(_compVect1, points, i);

            if (_compVect1.x < minX)
                minX = _compVect1.x;
            else if (_compVect1.x > maxX)
                maxX = _compVect1.x;

            if (_compVect1.y < minY)
                minY = _compVect1.y;
            else if (_compVect1.y > maxY)
                maxY = _compVect1.y;
        }

        center.set(minX + maxX, minY + maxY, 0);
        center.multLocal(0.5f);

        extent.set(maxX - center.x, maxY - center.y, 0);

        xAxis.set(1, 0, 0);
        yAxis.set(0, 1, 0);
       
        correctCorners = false;
    }

    public BoundingVolume merge(BoundingVolume volume) {
        // clone ourselves into a new bounding volume, then merge.
        return clone(new OrthogonalBoundingBox()).mergeLocal(volume);
    }

    public BoundingVolume mergeLocal(BoundingVolume volume) {
        if (volume == null)
            return this;

        switch (volume.getType()) {

            case OrthogonalBoundingBox: {
                return mergeOBB((OrthogonalBoundingBox) volume);
            }
            default:
                return null;
        }
    }

    
    private BoundingVolume mergeOBB(OrthogonalBoundingBox volume) {
        OrthogonalBoundingBox rkBox0 = this;
        OrthogonalBoundingBox rkBox1 = volume;

        Vector3f kBoxCenter = (rkBox0.center.add(rkBox1.center, _compVect7))
                .multLocal(.5f);

        Quaternion kQ0 = tempQa, kQ1 = tempQb;
        kQ0.fromAxes(rkBox0.xAxis, rkBox0.yAxis, rkBox0.zAxis);
        kQ1.fromAxes(rkBox1.xAxis, rkBox1.yAxis, rkBox1.zAxis);

        if (kQ0.dot(kQ1) < 0.0f)
            kQ1.negate();

        Quaternion kQ = kQ0.addLocal(kQ1);
        kQ.normalize();

        int i;
        float fDot;
        Vector3f kDiff = _compVect4;
        Vector3f kMin = _compVect5;
        Vector3f kMax = _compVect6;
        kMin.zero();
        kMax.zero();

        if (!rkBox0.correctCorners)
            rkBox0.computeCorners();
        for (i = 0; i < 4; i++) {
            rkBox0.vectorStore[i].subtract(kBoxCenter, kDiff);

            fDot = kDiff.dot(xAxis);
            if (fDot > kMax.x)
                kMax.x = fDot;
            else if (fDot < kMin.x)
                kMin.x = fDot;

            fDot = kDiff.dot(yAxis);
            if (fDot > kMax.y)
                kMax.y = fDot;
            else if (fDot < kMin.y)
                kMin.y = fDot;

        }

        if (!rkBox1.correctCorners)
            rkBox1.computeCorners();
        for (i = 0; i < 4; i++) {
            rkBox1.vectorStore[i].subtract(kBoxCenter, kDiff);

            fDot = kDiff.dot(xAxis);
            if (fDot > kMax.x)
                kMax.x = fDot;
            else if (fDot < kMin.x)
                kMin.x = fDot;

            fDot = kDiff.dot(yAxis);
            if (fDot > kMax.y)
                kMax.y = fDot;
            else if (fDot < kMin.y)
                kMin.y = fDot;
        }

        this.xAxis.set(xAxis);
        this.yAxis.set(yAxis);

        this.extent.x = .5f * (kMax.x - kMin.x);
        kBoxCenter.addLocal(this.xAxis.mult(.5f * (kMax.x + kMin.x), tempVe));

        this.extent.y = .5f * (kMax.y - kMin.y);
        kBoxCenter.addLocal(this.yAxis.mult(.5f * (kMax.y + kMin.y), tempVe));

        this.center.set(kBoxCenter);

        this.correctCorners = false;
        return this;
    }

    public BoundingVolume clone(BoundingVolume store) {
        OrthogonalBoundingBox toReturn;
        if (store instanceof OrthogonalBoundingBox) {
            toReturn = (OrthogonalBoundingBox) store;
        } else {
            toReturn = new OrthogonalBoundingBox();
        }
        toReturn.extent.set(extent);
        toReturn.xAxis.set(xAxis);
        toReturn.yAxis.set(yAxis);
        toReturn.center.set(center);
        toReturn.checkPlane = checkPlane;
        for (int x = vectorStore.length; --x >= 0; )
            toReturn.vectorStore[x].set(vectorStore[x]);
        toReturn.correctCorners = this.correctCorners;
        return toReturn;
    }

    /**
     * Sets the vectorStore information to the 4 corners of the box.
     */
    public void computeCorners() {
        Vector3f akEAxis0 = xAxis.mult(extent.x, _compVect1);
        Vector3f akEAxis1 = yAxis.mult(extent.y, _compVect2);
        
        vectorStore[0].set(center).subtractLocal(akEAxis0).subtractLocal(akEAxis1);
        vectorStore[1].set(center).addLocal(akEAxis0).subtractLocal(akEAxis1);
        vectorStore[2].set(center).addLocal(akEAxis0).addLocal(akEAxis1);
        vectorStore[3].set(center).subtractLocal(akEAxis0).addLocal(akEAxis1);
        correctCorners = true;
    }
    
    public void computeFromTris(int[] indices, TriMesh mesh, int start, int end) {
        if (end - start <= 0) {
            return;
        }
        Vector3f[] verts = new Vector3f[3];
        Vector3f min = _compVect1.set(new Vector3f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY));
        Vector3f max = _compVect2.set(new Vector3f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
        Vector3f point;
        for (int i = start; i < end; i++) {
        	mesh.getTriangle(indices[i], verts);
            point = verts[0];
            if (point.x < min.x)
                min.x = point.x;
            else if (point.x > max.x)
                max.x = point.x;
            if (point.y < min.y)
                min.y = point.y;
            else if (point.y > max.y)
                max.y = point.y;

            point = verts[1];
            if (point.x < min.x)
                min.x = point.x;
            else if (point.x > max.x)
                max.x = point.x;
            if (point.y < min.y)
                min.y = point.y;
            else if (point.y > max.y)
                max.y = point.y;

            point = verts[2];
            if (point.x < min.x)
                min.x = point.x;
            else if (point.x > max.x)
                max.x = point.x;

            if (point.y < min.y)
                min.y = point.y;
            else if (point.y > max.y)
                max.y = point.y;
        }

        center.set(min.addLocal(max));
        center.multLocal(0.5f);

        extent.set(max.x - center.x, max.y - center.y, 0);

        xAxis.set(1, 0, 0);
        yAxis.set(0, 1, 0);
        
        correctCorners = false;
    }

    public void computeFromTris(Triangle[] tris, int start, int end) {
        if (end - start <= 0) {
            return;
        }

        Vector3f min = _compVect1.set(tris[start].get(0));
        Vector3f max = _compVect2.set(min);
        Vector3f point;
        for (int i = start; i < end; i++) {

            point = tris[i].get(0);
            if (point.x < min.x)
                min.x = point.x;
            else if (point.x > max.x)
                max.x = point.x;
            if (point.y < min.y)
                min.y = point.y;
            else if (point.y > max.y)
                max.y = point.y;

            point = tris[i].get(1);
            if (point.x < min.x)
                min.x = point.x;
            else if (point.x > max.x)
                max.x = point.x;
            if (point.y < min.y)
                min.y = point.y;
            else if (point.y > max.y)
                max.y = point.y;

            point = tris[i].get(2);
            if (point.x < min.x)
                min.x = point.x;
            else if (point.x > max.x)
                max.x = point.x;

            if (point.y < min.y)
                min.y = point.y;
            else if (point.y > max.y)
                max.y = point.y;
        }

        center.set(min.addLocal(max));
        center.multLocal(0.5f);

        extent.set(max.x - center.x, max.y - center.y, 0);

        xAxis.set(1, 0, 0);
        yAxis.set(0, 1, 0);
        
        correctCorners = false;
    }

    public boolean intersection(OrthogonalBoundingBox box1) {
        // Cutoff for cosine of angles between box axes. This is used to catch
        // the cases when at least one pair of axes are parallel. If this
        // happens,
        // there is no need to test for separation along the Cross(A[i],B[j])
        // directions.
        OrthogonalBoundingBox box0 = this;
        float cutoff = 0.999999f;
        boolean parallelPairExists = false;
        int i;

        // convenience variables
        Vector3f akA[] = new Vector3f[] { box0.xAxis, box0.yAxis, box0.zAxis };
        Vector3f[] akB = new Vector3f[] { box1.xAxis, box1.yAxis, box1.zAxis };
        Vector3f afEA = box0.extent;
        Vector3f afEB = box1.extent;

        // compute difference of box centers, D = C1-C0
        Vector3f kD = box1.center.subtract(box0.center, _compVect1);

        float[][] aafC = { fWdU, fAWdU, fDdU };

        float[][] aafAbsC = { fADdU, fAWxDdU, tempFa };

        float[] afAD = tempFb;
        float fR0, fR1, fR; // interval radii and distance between centers
        float fR01; // = R0 + R1

        // axis C0+t*A0
        for (i = 0; i < 3; i++) {
            aafC[0][i] = akA[0].dot(akB[i]);
            aafAbsC[0][i] = FastMath.abs(aafC[0][i]);
            if (aafAbsC[0][i] > cutoff) {
                parallelPairExists = true;
            }
        }
        afAD[0] = akA[0].dot(kD);
        fR = FastMath.abs(afAD[0]);
        fR1 = afEB.x * aafAbsC[0][0] + afEB.y * aafAbsC[0][1] + afEB.z
                * aafAbsC[0][2];
        fR01 = afEA.x + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A1
        for (i = 0; i < 3; i++) {
            aafC[1][i] = akA[1].dot(akB[i]);
            aafAbsC[1][i] = FastMath.abs(aafC[1][i]);
            if (aafAbsC[1][i] > cutoff) {
                parallelPairExists = true;
            }
        }
        afAD[1] = akA[1].dot(kD);
        fR = FastMath.abs(afAD[1]);
        fR1 = afEB.x * aafAbsC[1][0] + afEB.y * aafAbsC[1][1] + afEB.z
                * aafAbsC[1][2];
        fR01 = afEA.y + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A2
        for (i = 0; i < 3; i++) {
            aafC[2][i] = akA[2].dot(akB[i]);
            aafAbsC[2][i] = FastMath.abs(aafC[2][i]);
            if (aafAbsC[2][i] > cutoff) {
                parallelPairExists = true;
            }
        }
        afAD[2] = akA[2].dot(kD);
        fR = FastMath.abs(afAD[2]);
        fR1 = afEB.x * aafAbsC[2][0] + afEB.y * aafAbsC[2][1] + afEB.z
                * aafAbsC[2][2];
        fR01 = afEA.z + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*B0
        fR = FastMath.abs(akB[0].dot(kD));
        fR0 = afEA.x * aafAbsC[0][0] + afEA.y * aafAbsC[1][0] + afEA.z
                * aafAbsC[2][0];
        fR01 = fR0 + afEB.x;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*B1
        fR = FastMath.abs(akB[1].dot(kD));
        fR0 = afEA.x * aafAbsC[0][1] + afEA.y * aafAbsC[1][1] + afEA.z
                * aafAbsC[2][1];
        fR01 = fR0 + afEB.y;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*B2
        fR = FastMath.abs(akB[2].dot(kD));
        fR0 = afEA.x * aafAbsC[0][2] + afEA.y * aafAbsC[1][2] + afEA.z
                * aafAbsC[2][2];
        fR01 = fR0 + afEB.z;
        if (fR > fR01) {
            return false;
        }

        // At least one pair of box axes was parallel, so the separation is
        // effectively in 2D where checking the "edge" normals is sufficient for
        // the separation of the boxes.
        if (parallelPairExists) {
            return true;
        }

        // axis C0+t*A0xB0
        fR = FastMath.abs(afAD[2] * aafC[1][0] - afAD[1] * aafC[2][0]);
        fR0 = afEA.y * aafAbsC[2][0] + afEA.z * aafAbsC[1][0];
        fR1 = afEB.y * aafAbsC[0][2] + afEB.z * aafAbsC[0][1];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A0xB1
        fR = FastMath.abs(afAD[2] * aafC[1][1] - afAD[1] * aafC[2][1]);
        fR0 = afEA.y * aafAbsC[2][1] + afEA.z * aafAbsC[1][1];
        fR1 = afEB.x * aafAbsC[0][2] + afEB.z * aafAbsC[0][0];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A0xB2
        fR = FastMath.abs(afAD[2] * aafC[1][2] - afAD[1] * aafC[2][2]);
        fR0 = afEA.y * aafAbsC[2][2] + afEA.z * aafAbsC[1][2];
        fR1 = afEB.x * aafAbsC[0][1] + afEB.y * aafAbsC[0][0];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A1xB0
        fR = FastMath.abs(afAD[0] * aafC[2][0] - afAD[2] * aafC[0][0]);
        fR0 = afEA.x * aafAbsC[2][0] + afEA.z * aafAbsC[0][0];
        fR1 = afEB.y * aafAbsC[1][2] + afEB.z * aafAbsC[1][1];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A1xB1
        fR = FastMath.abs(afAD[0] * aafC[2][1] - afAD[2] * aafC[0][1]);
        fR0 = afEA.x * aafAbsC[2][1] + afEA.z * aafAbsC[0][1];
        fR1 = afEB.x * aafAbsC[1][2] + afEB.z * aafAbsC[1][0];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A1xB2
        fR = FastMath.abs(afAD[0] * aafC[2][2] - afAD[2] * aafC[0][2]);
        fR0 = afEA.x * aafAbsC[2][2] + afEA.z * aafAbsC[0][2];
        fR1 = afEB.x * aafAbsC[1][1] + afEB.y * aafAbsC[1][0];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A2xB0
        fR = FastMath.abs(afAD[1] * aafC[0][0] - afAD[0] * aafC[1][0]);
        fR0 = afEA.x * aafAbsC[1][0] + afEA.y * aafAbsC[0][0];
        fR1 = afEB.y * aafAbsC[2][2] + afEB.z * aafAbsC[2][1];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A2xB1
        fR = FastMath.abs(afAD[1] * aafC[0][1] - afAD[0] * aafC[1][1]);
        fR0 = afEA.x * aafAbsC[1][1] + afEA.y * aafAbsC[0][1];
        fR1 = afEB.x * aafAbsC[2][2] + afEB.z * aafAbsC[2][0];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A2xB2
        fR = FastMath.abs(afAD[1] * aafC[0][2] - afAD[0] * aafC[1][2]);
        fR0 = afEA.x * aafAbsC[1][2] + afEA.y * aafAbsC[0][2];
        fR1 = afEB.x * aafAbsC[2][1] + afEB.y * aafAbsC[2][0];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jme.bounding.BoundingVolume#intersects(com.jme.bounding.BoundingVolume)
     */
    public boolean intersects(BoundingVolume bv) {
		if(bv == null || bv.getType() != Type.OrthogonalBoundingBox)
			return false;
      
 		OrthogonalBoundingBox obb = (OrthogonalBoundingBox) bv;
		
        if (!Vector3f.isValidVector(center) || !Vector3f.isValidVector(obb.center)) return false;
        

        // Cutoff for cosine of angles between box axes. This is used to catch
        // the cases when at least one pair of axes are parallel. If this
        // happens,
        // there is no need to test for separation along the Cross(A[i],B[j])
        // directions.
        float cutoff = 0.999999f;
        boolean parallelPairExists = false;
        int i;

        // convenience variables
        Vector3f akA[] = new Vector3f[] { xAxis, yAxis, zAxis };
        Vector3f[] akB = new Vector3f[] { obb.xAxis, obb.yAxis, obb.zAxis };
        Vector3f afEA = extent;
        Vector3f afEB = obb.extent;

        // compute difference of box centers, D = C1-C0
        Vector3f kD = obb.center.subtract(center, _compVect1);

        float[][] aafC = { fWdU, fAWdU, fDdU };

        float[][] aafAbsC = { fADdU, fAWxDdU, tempFa };

        float[] afAD = tempFb;
        float fR0, fR1, fR; // interval radii and distance between centers
        float fR01; // = R0 + R1

        // axis C0+t*A0
        for (i = 0; i < 3; i++) {
            aafC[0][i] = akA[0].dot(akB[i]);
            aafAbsC[0][i] = FastMath.abs(aafC[0][i]);
            if (aafAbsC[0][i] > cutoff) {
                parallelPairExists = true;
            }
        }
        afAD[0] = akA[0].dot(kD);
        fR = FastMath.abs(afAD[0]);
        fR1 = afEB.x * aafAbsC[0][0] + afEB.y * aafAbsC[0][1] + afEB.z
                * aafAbsC[0][2];
        fR01 = afEA.x + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A1
        for (i = 0; i < 3; i++) {
            aafC[1][i] = akA[1].dot(akB[i]);
            aafAbsC[1][i] = FastMath.abs(aafC[1][i]);
            if (aafAbsC[1][i] > cutoff) {
                parallelPairExists = true;
            }
        }
        afAD[1] = akA[1].dot(kD);
        fR = FastMath.abs(afAD[1]);
        fR1 = afEB.x * aafAbsC[1][0] + afEB.y * aafAbsC[1][1] + afEB.z
                * aafAbsC[1][2];
        fR01 = afEA.y + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A2
        for (i = 0; i < 3; i++) {
            aafC[2][i] = akA[2].dot(akB[i]);
            aafAbsC[2][i] = FastMath.abs(aafC[2][i]);
            if (aafAbsC[2][i] > cutoff) {
                parallelPairExists = true;
            }
        }
        afAD[2] = akA[2].dot(kD);
        fR = FastMath.abs(afAD[2]);
        fR1 = afEB.x * aafAbsC[2][0] + afEB.y * aafAbsC[2][1] + afEB.z
                * aafAbsC[2][2];
        fR01 = afEA.z + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*B0
        fR = FastMath.abs(akB[0].dot(kD));
        fR0 = afEA.x * aafAbsC[0][0] + afEA.y * aafAbsC[1][0] + afEA.z
                * aafAbsC[2][0];
        fR01 = fR0 + afEB.x;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*B1
        fR = FastMath.abs(akB[1].dot(kD));
        fR0 = afEA.x * aafAbsC[0][1] + afEA.y * aafAbsC[1][1] + afEA.z
                * aafAbsC[2][1];
        fR01 = fR0 + afEB.y;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*B2
        fR = FastMath.abs(akB[2].dot(kD));
        fR0 = afEA.x * aafAbsC[0][2] + afEA.y * aafAbsC[1][2] + afEA.z
                * aafAbsC[2][2];
        fR01 = fR0 + afEB.z;
        if (fR > fR01) {
            return false;
        }

        // At least one pair of box axes was parallel, so the separation is
        // effectively in 2D where checking the "edge" normals is sufficient for
        // the separation of the boxes.
        if (parallelPairExists) {
            return true;
        }

        // axis C0+t*A0xB0
        fR = FastMath.abs(afAD[2] * aafC[1][0] - afAD[1] * aafC[2][0]);
        fR0 = afEA.y * aafAbsC[2][0] + afEA.z * aafAbsC[1][0];
        fR1 = afEB.y * aafAbsC[0][2] + afEB.z * aafAbsC[0][1];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A0xB1
        fR = FastMath.abs(afAD[2] * aafC[1][1] - afAD[1] * aafC[2][1]);
        fR0 = afEA.y * aafAbsC[2][1] + afEA.z * aafAbsC[1][1];
        fR1 = afEB.x * aafAbsC[0][2] + afEB.z * aafAbsC[0][0];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A0xB2
        fR = FastMath.abs(afAD[2] * aafC[1][2] - afAD[1] * aafC[2][2]);
        fR0 = afEA.y * aafAbsC[2][2] + afEA.z * aafAbsC[1][2];
        fR1 = afEB.x * aafAbsC[0][1] + afEB.y * aafAbsC[0][0];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A1xB0
        fR = FastMath.abs(afAD[0] * aafC[2][0] - afAD[2] * aafC[0][0]);
        fR0 = afEA.x * aafAbsC[2][0] + afEA.z * aafAbsC[0][0];
        fR1 = afEB.y * aafAbsC[1][2] + afEB.z * aafAbsC[1][1];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A1xB1
        fR = FastMath.abs(afAD[0] * aafC[2][1] - afAD[2] * aafC[0][1]);
        fR0 = afEA.x * aafAbsC[2][1] + afEA.z * aafAbsC[0][1];
        fR1 = afEB.x * aafAbsC[1][2] + afEB.z * aafAbsC[1][0];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A1xB2
        fR = FastMath.abs(afAD[0] * aafC[2][2] - afAD[2] * aafC[0][2]);
        fR0 = afEA.x * aafAbsC[2][2] + afEA.z * aafAbsC[0][2];
        fR1 = afEB.x * aafAbsC[1][1] + afEB.y * aafAbsC[1][0];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A2xB0
        fR = FastMath.abs(afAD[1] * aafC[0][0] - afAD[0] * aafC[1][0]);
        fR0 = afEA.x * aafAbsC[1][0] + afEA.y * aafAbsC[0][0];
        fR1 = afEB.y * aafAbsC[2][2] + afEB.z * aafAbsC[2][1];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A2xB1
        fR = FastMath.abs(afAD[1] * aafC[0][1] - afAD[0] * aafC[1][1]);
        fR0 = afEA.x * aafAbsC[1][1] + afEA.y * aafAbsC[0][1];
        fR1 = afEB.x * aafAbsC[2][2] + afEB.z * aafAbsC[2][0];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        // axis C0+t*A2xB2
        fR = FastMath.abs(afAD[1] * aafC[0][2] - afAD[0] * aafC[1][2]);
        fR0 = afEA.x * aafAbsC[1][2] + afEA.y * aafAbsC[0][2];
        fR1 = afEB.x * aafAbsC[2][1] + afEB.y * aafAbsC[2][0];
        fR01 = fR0 + fR1;
        if (fR > fR01) {
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jme.bounding.BoundingVolume#intersectsSphere(com.jme.bounding.BoundingSphere)
     */
    public boolean intersectsSphere(BoundingSphere bs) {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jme.bounding.BoundingVolume#intersectsBoundingBox(com.jme.bounding.BoundingBox)
     */
    public boolean intersectsBoundingBox(BoundingBox bb) {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jme.bounding.BoundingVolume#intersectsOBB2(com.jme.bounding.OBB2)
     */
 
    
    public boolean intersectsCapsule(BoundingCapsule bc) {
    	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jme.bounding.BoundingVolume#intersects(com.jme.math.Ray)
     */
    public boolean intersects(Ray ray) {
		ray.intersectsWherePlane(XYPlane , _compVect1);
		_compVect2.set(DisplaySystem.getDisplaySystem().getScreenCoordinates(_compVect1));
		_compVect2.setZ(0);
		return contains(_compVect2);
    }

    /**
     * @see com.jme.bounding.BoundingVolume#intersectsWhere(com.jme.math.Ray)
     */
    public IntersectionRecord intersectsWhere(Ray ray) {
        Vector3f diff = _compVect1.set(ray.origin).subtractLocal(center);
        // convert ray to box coordinates
        Vector3f direction = _compVect2.set(ray.direction.x, ray.direction.y,
                ray.direction.z);
        float[] t = { 0f, Float.POSITIVE_INFINITY };
        
        float saveT0 = t[0], saveT1 = t[1];
        boolean notEntirelyClipped = clip(+direction.x, -diff.x - extent.x, t)
                && clip(-direction.x, +diff.x - extent.x, t)
                && clip(+direction.y, -diff.y - extent.y, t)
                && clip(-direction.y, +diff.y - extent.y, t);
        
        if (notEntirelyClipped && (t[0] != saveT0 || t[1] != saveT1)) {
            if (t[1] > t[0]) {
                float[] distances = t;
                Vector3f[] points = new Vector3f[] { 
                        new Vector3f(ray.direction).multLocal(distances[0]).addLocal(ray.origin),
                        new Vector3f(ray.direction).multLocal(distances[1]).addLocal(ray.origin)
                        };
                IntersectionRecord record = new IntersectionRecord(distances, points);
                return record;
            }
                
            float[] distances = new float[] { t[0] };
            Vector3f[] points = new Vector3f[] { 
                    new Vector3f(ray.direction).multLocal(distances[0]).addLocal(ray.origin),
                    };
            IntersectionRecord record = new IntersectionRecord(distances, points);
            return record;            
        } 
            
        return new IntersectionRecord();        

    }

    /**
     * <code>clip</code> determines if a line segment intersects the current
     * test plane.
     * 
     * @param denom
     *            the denominator of the line segment.
     * @param numer
     *            the numerator of the line segment.
     * @param t
     *            test values of the plane.
     * @return true if the line segment intersects the plane, false otherwise.
     */
    private boolean clip(float denom, float numer, float[] t) {
        // Return value is 'true' if line segment intersects the current test
        // plane. Otherwise 'false' is returned in which case the line segment
        // is entirely clipped.
        if (denom > 0.0f) {
            if (numer > denom * t[1])
                return false;
            if (numer > denom * t[0])
                t[0] = numer / denom;
            return true;
        } else if (denom < 0.0f) {
            if (numer > denom * t[0])
                return false;
            if (numer > denom * t[1])
                t[1] = numer / denom;
            return true;
        } else {
            return numer <= 0.0;
        }
    }

    public void setXAxis(Vector3f axis) {
        xAxis.set(axis);
        correctCorners = false;
    }

    public void setYAxis(Vector3f axis) {
        yAxis.set(axis);
        correctCorners = false;
    }

    public void setZAxis(Vector3f axis) {
        zAxis.set(axis);
        correctCorners = false;
    }

    public void setExtent(Vector3f ext) {
        extent.set(ext);
        correctCorners = false;
    }

    public Vector3f getXAxis() {
        return xAxis;
    }

    public Vector3f getYAxis() {
        return yAxis;
    }

    public Vector3f getZAxis() {
        return zAxis;
    }

    public Vector3f getExtent() {
        return extent;
    }

    @Override
    public boolean contains(Vector3f point) {
        _compVect1.set(point).subtractLocal(center);
        float coeff = _compVect1.dot(xAxis);
        if (FastMath.abs(coeff) > extent.x) return false;
        
        coeff = _compVect1.dot(yAxis);
        if (FastMath.abs(coeff) > extent.y) return false;
        
        return true;
    }
    
    @Override
    public float distanceToEdge(Vector3f point) {
        // compute coordinates of point in box coordinate system
        Vector3f diff = point.subtract(center);
        Vector3f closest = new Vector3f(diff.dot(xAxis), diff.dot(yAxis), diff
                .dot(zAxis));

        // project test point onto box
        float sqrDistance = 0.0f;
        float delta;

        if (closest.x < -extent.x) {
            delta = closest.x + extent.x;
            sqrDistance += delta * delta;
            closest.x = -extent.x;
        } else if (closest.x > extent.x) {
            delta = closest.x - extent.x;
            sqrDistance += delta * delta;
            closest.x = extent.x;
        }

        if (closest.y < -extent.y) {
            delta = closest.y + extent.y;
            sqrDistance += delta * delta;
            closest.y = -extent.y;
        } else if (closest.y > extent.y) {
            delta = closest.y - extent.y;
            sqrDistance += delta * delta;
            closest.y = extent.y;
        }

        return FastMath.sqrt(sqrDistance);
    }

    public void write(JMEExporter e) throws IOException {
        super.write(e);
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(xAxis, "xAxis", Vector3f.UNIT_X);
        capsule.write(yAxis, "yAxis", Vector3f.UNIT_Y);
        capsule.write(extent, "extent", Vector3f.ZERO);
    }

    public void read(JMEImporter e) throws IOException {
        super.read(e);
        InputCapsule capsule = e.getCapsule(this);
        xAxis.set((Vector3f) capsule.readSavable("xAxis", Vector3f.UNIT_X.clone()));
        yAxis.set((Vector3f) capsule.readSavable("yAxis", Vector3f.UNIT_Y.clone()));
        extent.set((Vector3f) capsule.readSavable("extent", Vector3f.ZERO.clone()));
        correctCorners = false;
    }

    @Override
    public float getVolume() {
        return extent.x*extent.y;
    }

	@Override
	public boolean intersectsOrientedBoundingBox(OrientedBoundingBox bb) {
		// TODO Auto-generated method stub
		return false;
	}
}