/*******************************************************************************
 * This file is part of the GeOxygene project source files.
 * 
 * GeOxygene aims at providing an open framework which implements OGC/ISO
 * specifications for the development and deployment of geographic (GIS)
 * applications. It is a open source contribution of the COGIT laboratory at the
 * Institut Géographique National (the French National Mapping Agency).
 * 
 * See: http://oxygene-project.sourceforge.net
 * 
 * Copyright (C) 2005 Institut Géographique National
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library (see file LICENSE if present); if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 *******************************************************************************/

package fr.ign.cogit.geoxygene.appli.gl;

import static org.lwjgl.util.glu.GLU.GLU_TESS_BEGIN;
import static org.lwjgl.util.glu.GLU.GLU_TESS_COMBINE;
import static org.lwjgl.util.glu.GLU.GLU_TESS_END;
import static org.lwjgl.util.glu.GLU.GLU_TESS_VERTEX;
import static org.lwjgl.util.glu.GLU.gluNewTess;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point2d;

import org.apache.log4j.Logger;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.GLUtessellator;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IRing;
import fr.ign.cogit.geoxygene.appli.Viewport;
import fr.ign.cogit.geoxygene.appli.render.primitive.Parameterizer;
import fr.ign.cogit.geoxygene.style.Stroke;
import fr.ign.cogit.geoxygene.style.Symbolizer;
import fr.ign.cogit.geoxygene.util.gl.GLComplex;
import fr.ign.cogit.geoxygene.util.gl.GLPrimitiveTessCallback;

/**
 * @author JeT
 *         Utility functions for GL drawing
 */
public class GLComplexFactory {

    private static final Logger logger = Logger.getLogger(GLComplexFactory.class.getName()); // logger
    private static final int BEZIER_SAMPLE_COUNT = 10;
    private static final Point2d nullUV = new Point2d(0, 0);

    /**
     * Private constructor for utility class
     */
    private GLComplexFactory() {
        // utility class
    }

    // cubic bernstein polynom
    static private float bernstein3(int i, float t) {
        switch (i) {
        case 0:
            return (1 - t) * (1 - t) * (1 - t);
        case 1:
            return 3 * t * (1 - t) * (1 - t);
        case 2:
            return 3 * t * t * (1 - t);
        case 3:
            return t * t * t;
        }
        return 0; //we only get here if an invalid i is specified
    }

    // quadratic bernstein polynom
    static private float bernstein2(int i, float t) {
        switch (i) {
        case 0:
            return (1 - t) * (1 - t);
        case 1:
            return 2 * t * (1 - t);
        case 2:
            return t * t;
        }
        return 0; //we only get here if an invalid i is specified
    }

    //evaluate a point on the B spline
    private static double interpolateQuadratic(double v0, double v1, double v2, float t) {
        return bernstein2(0, t) * v0 + bernstein2(1, t) * v1 + bernstein2(2, t) * v2;
    }

    //evaluate a point on the B spline
    private static double interpolateCubic(double v0, double v1, double v2, double v3, float t) {
        return bernstein3(0, t) * v0 + bernstein3(1, t) * v1 + bernstein3(2, t) * v2 + bernstein3(3, t) * v3;
    }

    //    /**
    //     * Tesselate a java shape2D into a GLPrimitive
    //     */
    //    public static GLComplex createGLTrianglePrimitive() {
    //        GLComplex primitive = new GLComplex();
    //        GLMesh mesh = primitive.addGLMesh(GL11.GL_TRIANGLES);
    //        int vertex1 = primitive.addVertex(new GLVertex(0.1f, 0.1f, 0.1f));
    //        int vertex2 = primitive.addVertex(new GLVertex(.1f, .8f, 0.f));
    //        int vertex3 = primitive.addVertex(new GLVertex(.8f, .8f, 0.f));
    //        mesh.addIndices(vertex1, vertex2, vertex3);
    //        return primitive;
    //    }

    /**
     * Tesselate a list of java shape2D into a GLPrimitive
     */
    public static GLComplex toGLComplex(final List<Path2D> outlines) {

        GLComplex primitive = new GLComplex();
        for (Path2D path : outlines) {
            primitive.addGLComplex(toGLComplex(path));
        }
        return primitive;
    }

    /**
     * Tesselate a java shape2D into a GLPrimitive
     */
    public static GLComplex toGLComplex(final Path2D shape) {

        GLComplex primitive = new GLComplex();

        // tesselation
        GLUtessellator tesselator = gluNewTess();

        // Set callback functions
        GLPrimitiveTessCallback callback = new GLPrimitiveTessCallback(primitive);
        tesselator.gluTessCallback(GLU_TESS_VERTEX, callback);
        tesselator.gluTessCallback(GLU_TESS_BEGIN, callback);
        tesselator.gluTessCallback(GLU_TESS_END, callback);
        tesselator.gluTessCallback(GLU_TESS_COMBINE, callback);
        switch (shape.getWindingRule()) {
        case Path2D.WIND_EVEN_ODD:
            tesselator.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_ODD);
            break;
        case Path2D.WIND_NON_ZERO:
            tesselator.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_NONZERO);
            break;
        default:
            logger.warn("unknown winding rule " + shape.getWindingRule());
        }

        tesselator.gluTessBeginPolygon(null);

        float lastX = 0;
        float lastY = 0;
        float lastMoveX = 0;
        float lastMoveY = 0;

        double[] vertex = null;
        float[] data = null;
        float[] coords = new float[6];

        PathIterator iter = shape.getPathIterator(null); // ,5) add a number on here to simplify verts

        int rule = iter.getWindingRule();
        switch (rule) {
        case PathIterator.WIND_EVEN_ODD:
            tesselator.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_ODD);
            break;
        case PathIterator.WIND_NON_ZERO:
            tesselator.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_NONZERO);
            break;
        }
        //        System.err.println("----------------------------------------------------------------------------");
        while (!iter.isDone()) {

            switch (iter.currentSegment(coords)) {

            case PathIterator.SEG_MOVETO:   // 1 point (2 vars) in coords
                tesselator.gluTessBeginContour();
                lastX = lastMoveX = coords[0];
                lastY = lastMoveY = coords[1];
                vertex = new double[] { coords[0], coords[1], 0. };
                data = new float[] { coords[0], coords[1], 0, 0, 0, 0, 0, 0, 0 };
                tesselator.gluTessVertex(vertex, 0, data);
                //                System.err.println("MOVETO " + coords[0] + "x" + coords[1]);
                break;
            case PathIterator.SEG_LINETO:   // 1 point
                vertex = new double[] { coords[0], coords[1], 0. };
                data = new float[] { coords[0], coords[1], 0, 0, 0, 0, 0, 0, 0 };
                tesselator.gluTessVertex(vertex, 0, data);
                lastX = coords[0];
                lastY = coords[1];
                //                System.err.println("LINETO " + coords[0] + "x" + coords[1]);
                break;

            case PathIterator.SEG_QUADTO:   // 2 points (1 control point)
                //                System.err.println("QUAD FROM " + lastX + "x" + lastY + " TO " + coords[2] + "x" + coords[3]);
                for (int i = 1; i <= BEZIER_SAMPLE_COUNT; i++) {
                    float t = i / (float) BEZIER_SAMPLE_COUNT;

                    double px = interpolateQuadratic(lastX, coords[0], coords[2], t);
                    double py = interpolateQuadratic(lastY, coords[1], coords[3], t);
                    vertex = new double[] { px, py, 0 };
                    data = new float[] { (float) px, (float) py, 0, 0, 0, 0, 0, 0, 0 };
                    tesselator.gluTessVertex(vertex, 0, data);
                }
                lastX = coords[2];
                lastY = coords[3];
                break;

            case PathIterator.SEG_CUBICTO:  // 3 points (2 control points)
                //                System.err.println("CUBIC FROM " + lastX + "x" + lastY + " TO " + coords[4] + "x" + coords[5]);
                for (int i = 1; i <= BEZIER_SAMPLE_COUNT; i++) {
                    float t = i / (float) BEZIER_SAMPLE_COUNT;

                    double px = interpolateCubic(lastX, coords[0], coords[2], coords[4], t);
                    double py = interpolateCubic(lastY, coords[1], coords[3], coords[5], t);
                    vertex = new double[] { px, py, 0 };
                    data = new float[] { (float) px, (float) py, 0, 0, 0, 0, 0, 0, 0 };
                    tesselator.gluTessVertex(vertex, 0, data);
                }
                lastX = coords[4];
                lastY = coords[5];
                break;

            case PathIterator.SEG_CLOSE:
                lastX = lastMoveX;
                lastY = lastMoveY;
                tesselator.gluTessEndContour();
                //                System.err.println("CLOSE " + lastMoveX + "x" + lastMoveY);
                break;
            }
            iter.next();
        }
        tesselator.gluTessEndPolygon();

        //        System.err.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ vertices");
        //        FloatBuffer verticesBuffer = primitive.getFlippedVerticesBuffer();
        //        for (int i = 0; i < verticesBuffer.limit(); i += 9) {
        //            System.err.println("#" + (i / 9) + ": " + verticesBuffer.get(i) + "x" + verticesBuffer.get(i + 1));
        //        }
        //
        //        System.err.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ indices");
        //        IntBuffer indicesBuffer = primitive.getFlippedIndicesBuffer();
        //        for (int i = 0; i < indicesBuffer.limit(); i++) {
        //            System.err.println("#" + i + ": " + indicesBuffer.get(i));
        //        }
        //        System.err.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ meshes");
        //        for (GLMesh mesh : primitive.getMeshes()) {
        //
        //            System.err.println("type = " + mesh.getGlType() + " start = " + mesh.getFirstIndex() + " end = " + mesh.getLastIndex());
        //        }
        return primitive;
    }

    /**
     * Tesselate a java shape2D into a GLPrimitive
     */
    public static GLComplex createFilledPolygon(final IPolygon polygon, Parameterizer parameterizer) {

        GLComplex primitive = new GLComplex();

        // tesselation
        GLUtessellator tesselator = gluNewTess();

        // Set callback functions
        GLPrimitiveTessCallback callback = new GLPrimitiveTessCallback(primitive);
        tesselator.gluTessCallback(GLU_TESS_VERTEX, callback);
        tesselator.gluTessCallback(GLU_TESS_BEGIN, callback);
        tesselator.gluTessCallback(GLU_TESS_END, callback);
        tesselator.gluTessCallback(GLU_TESS_COMBINE, callback);
        tesselator.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_POSITIVE);

        tesselator.gluTessBeginPolygon(null);

        // outer frontier
        tesselator.gluTessBeginContour();
        for (int outerFrontierPointIndex = 0; outerFrontierPointIndex < polygon.exteriorCoord().size(); outerFrontierPointIndex++) {
            IDirectPosition outerFrontierPoint = polygon.exteriorCoord().get(outerFrontierPointIndex);
            //Point2d outerFrontierTextureCoordinates = polygon.getOuterFrontierTextureCoordinates(outerFrontierPointIndex);
            // point coordinates
            double vertex[] = new double[3];
            vertex[0] = outerFrontierPoint.getX();
            vertex[1] = outerFrontierPoint.getY();
            vertex[2] = 0; //outerFrontierPoint.getZ();
            Point2d uv = parameterizer != null ? parameterizer.getTextureCoordinates(vertex) : nullUV;
            float[] data = new float[] { (float) vertex[0], (float) vertex[1], (float) vertex[2], (float) uv.x, (float) uv.y, 0, 0, 0, 0 };
            tesselator.gluTessVertex(vertex, 0, data);
            //            System.err.println("set exterior vertex " + vertex[0] + ", " + vertex[1] + ", " + vertex[2]);
        }
        tesselator.gluTessEndContour();

        for (int innerFrontierIndex = 0; innerFrontierIndex < polygon.sizeInterior(); innerFrontierIndex++) {

            //            IRing innerFrontier = polygon.getInterior(innerFrontierIndex);
            tesselator.gluTessBeginContour();

            IDirectPositionList interiorCoord = polygon.interiorCoord(innerFrontierIndex);
            for (int innerFrontierPointIndex = 0; innerFrontierPointIndex < interiorCoord.size(); innerFrontierPointIndex++) {
                IDirectPosition innerFrontierPoint = interiorCoord.get(innerFrontierPointIndex);
                //Point2d innerFrontierTextureCoordinates = polygon.getInnerFrontierTextureCoordinates(innerFrontierIndex, innerFrontierPointIndex);

                double vertex[] = new double[3];
                vertex[0] = innerFrontierPoint.getX();
                vertex[1] = innerFrontierPoint.getY();
                vertex[2] = 0; // innerFrontierPoint.getZ();
                Point2d uv = parameterizer != null ? parameterizer.getTextureCoordinates(vertex) : nullUV;
                float[] data = new float[] { (float) vertex[0], (float) vertex[1], (float) vertex[2], (float) uv.x, (float) uv.y, 0, 0, 0, 0 };
                tesselator.gluTessVertex(vertex, 0, data);
                //                System.err.println("set interior #" + innerFrontierIndex + " vertex " + vertex[0] + ", " + vertex[1] + ", " + vertex[2]);
            }

            tesselator.gluTessEndContour();
        }
        tesselator.gluTessEndPolygon();

        return primitive;
    }

    /**
     * Tesselate a java shape2D into a GLPrimitive
     */
    public static GLComplex createFilledPolygon(final IPolygon polygon, Color color) {

        GLComplex primitive = new GLComplex();

        // tesselation
        GLUtessellator tesselator = gluNewTess();

        // Set callback functions
        GLPrimitiveTessCallback callback = new GLPrimitiveTessCallback(primitive);
        tesselator.gluTessCallback(GLU_TESS_VERTEX, callback);
        tesselator.gluTessCallback(GLU_TESS_BEGIN, callback);
        tesselator.gluTessCallback(GLU_TESS_END, callback);
        tesselator.gluTessCallback(GLU_TESS_COMBINE, callback);
        tesselator.gluTessProperty(GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_POSITIVE);

        tesselator.gluTessBeginPolygon(null);

        // outer frontier
        tesselator.gluTessBeginContour();
        for (int outerFrontierPointIndex = 0; outerFrontierPointIndex < polygon.exteriorCoord().size(); outerFrontierPointIndex++) {
            IDirectPosition outerFrontierPoint = polygon.exteriorCoord().get(outerFrontierPointIndex);
            //Point2d outerFrontierTextureCoordinates = polygon.getOuterFrontierTextureCoordinates(outerFrontierPointIndex);
            // point coordinates
            double vertex[] = new double[3];
            vertex[0] = outerFrontierPoint.getX();
            vertex[1] = outerFrontierPoint.getY();
            vertex[2] = 0; //outerFrontierPoint.getZ();
            Point2d uv = nullUV;
            float r = color.getRed() / 255f;
            float g = color.getGreen() / 255f;
            float b = color.getBlue() / 255f;
            float a = color.getTransparency() / 255f;
            float[] data = new float[] { (float) vertex[0], (float) vertex[1], (float) vertex[2], (float) uv.x, (float) uv.y, r, g, b, a };
            tesselator.gluTessVertex(vertex, 0, data);
            //            System.err.println("set exterior vertex " + vertex[0] + ", " + vertex[1] + ", " + vertex[2]);
        }
        tesselator.gluTessEndContour();

        for (int innerFrontierIndex = 0; innerFrontierIndex < polygon.sizeInterior(); innerFrontierIndex++) {

            //            IRing innerFrontier = polygon.getInterior(innerFrontierIndex);
            tesselator.gluTessBeginContour();

            IDirectPositionList interiorCoord = polygon.interiorCoord(innerFrontierIndex);
            for (int innerFrontierPointIndex = 0; innerFrontierPointIndex < interiorCoord.size(); innerFrontierPointIndex++) {
                IDirectPosition innerFrontierPoint = interiorCoord.get(innerFrontierPointIndex);
                //Point2d innerFrontierTextureCoordinates = polygon.getInnerFrontierTextureCoordinates(innerFrontierIndex, innerFrontierPointIndex);

                double vertex[] = new double[3];
                vertex[0] = innerFrontierPoint.getX();
                vertex[1] = innerFrontierPoint.getY();
                vertex[2] = 0; // innerFrontierPoint.getZ();
                Point2d uv = nullUV;
                float r = color.getRed() / 255f;
                float g = color.getGreen() / 255f;
                float b = color.getBlue() / 255f;
                float a = color.getTransparency() / 255f;
                float[] data = new float[] { (float) vertex[0], (float) vertex[1], (float) vertex[2], (float) uv.x, (float) uv.y, r, g, b, a };
                tesselator.gluTessVertex(vertex, 0, data);
                //                System.err.println("set interior #" + innerFrontierIndex + " vertex " + vertex[0] + ", " + vertex[1] + ", " + vertex[2]);
            }

            tesselator.gluTessEndContour();
        }
        tesselator.gluTessEndPolygon();

        return primitive;
    }

    /**
     * Create a gl primitive with a filled surface with given color
     * 
     * @param multiSurface
     * @param parameterizer
     * @return
     */
    public static GLComplex createFilledPolygon(IMultiSurface<?> multiSurface, final Color color) {
        GLComplex primitive = new GLComplex();

        for (IOrientableSurface surface : multiSurface) {
            if (IPolygon.class.isAssignableFrom(surface.getClass())) {
                GLComplex subComplex = createFilledPolygon((IPolygon) surface, color);
                primitive.addGLComplex(subComplex);
            } else {
                logger.warn("Multi surface content is not polygons: " + surface.getClass().getSimpleName());
            }
        }
        return primitive;
    }

    /**
     * Create a gl primitive with a filled surface with a parameterization
     * applied on
     * 
     * @param multiSurface
     * @param parameterizer
     * @return
     */
    public static GLComplex createFilledPolygon(IMultiSurface<?> multiSurface, Parameterizer parameterizer) {
        GLComplex primitive = new GLComplex();

        for (IOrientableSurface surface : multiSurface) {
            if (IPolygon.class.isAssignableFrom(surface.getClass())) {
                GLComplex subComplex = createFilledPolygon((IPolygon) surface, parameterizer);
                primitive.addGLComplex(subComplex);
            } else {
                logger.warn("Multi surface content is not polygons: " + surface.getClass().getSimpleName());
            }
        }
        return primitive;
    }

    public static GLComplex createMultiSurfaceOutline(IMultiSurface<?> multiSurface, BasicStroke stroke) {
        GLComplex primitive = new GLComplex();

        for (IOrientableSurface surface : multiSurface) {
            if (IPolygon.class.isAssignableFrom(surface.getClass())) {
                GLComplex subComplex = createPolygonOutline((IPolygon) surface, stroke);
                primitive.addGLComplex(subComplex);
            } else {
                logger.warn("Multi surface content is not polygons: " + surface.getClass().getSimpleName());
            }
        }
        return primitive;
    }

    public static GLComplex createPolygonOutline(IPolygon polygon, BasicStroke stroke) {
        List<Path2D> outlines = new ArrayList<Path2D>();
        outlines.add(stroke(toShape(polygon.getExterior()), stroke));
        for (IRing interior : polygon.getInterior()) {
            outlines.add(stroke(toShape(interior), stroke));
        }
        return toGLComplex(outlines);
    }

    private static Path2D stroke(Path2D shape, java.awt.Stroke stroke) {
        return (Path2D) stroke.createStrokedShape(shape);
    }

    public static BasicStroke geoxygeneStrokeToAWTStroke(Viewport viewport, Symbolizer symbolizer) {
        float strokeOpacity = symbolizer.getStroke().getStrokeOpacity();
        if (strokeOpacity > 0f) {

            double scale = 1;
            if (!symbolizer.getUnitOfMeasure().equalsIgnoreCase(Symbolizer.PIXEL)) {
                try {
                    scale = viewport.getModelToViewTransform().getScaleX();
                } catch (NoninvertibleTransformException e) {
                    e.printStackTrace();
                }
            }
            return (BasicStroke) symbolizer.getStroke().toAwtStroke((float) scale);
        }
        return null;
    }

    private static Path2D toShape(IRing ring) {
        GeneralPath path = new GeneralPath();
        boolean first = true;
        for (IDirectPosition p : ring.coord()) {
            if (first) {
                path.moveTo(p.getX(), p.getY());
                first = false;
            } else {
                path.lineTo(p.getX(), p.getY());
            }
        }
        return path;
    }
}
