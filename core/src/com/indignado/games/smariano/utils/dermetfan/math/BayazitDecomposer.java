package com.indignado.games.smariano.utils.dermetfan.math;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.indignado.games.smariano.utils.dermetfan.math.GeometryUtils.*;

/**
 * Taken from <a href="http://code.google.com/p/box2d-editor/source/browse/editor/src/aurelienribon/bodyeditor/maths/earclipping/bayazit/BayazitDecomposer.java">Aurelien Ribon's Physics Body Editor</a><br/>
 * <br/>
 * Convex decomposition algorithm created by <a href="http://mnbayazit.com/">Mark Bayazit</a>
 * <a href="http://mnbayazit.com/406/bayazit">more information</a> about this algorithm
 */
public abstract class BayazitDecomposer {

	public static final float EPSILON = 1.192092896e-07f;
	public static int maxPolygonVertices = 8;

	public static Vector2 cross(Vector2 a, float s) {
		return new Vector2(s * a.y, -s * a.x);
	}

	private static Vector2 at(int i, Array<Vector2> vertices) {
		return vertices.get(i < 0 ? vertices.size - -i % vertices.size : i % vertices.size);
	}

	private static Array<Vector2> copy(int i, int j, Array<Vector2> vertices) {
		Array<Vector2> p = new Array<Vector2>();
		while(j < i)
			j += vertices.size;
		// p.reserve(j - i + 1);
		for(; i <= j; ++i)
			p.add(at(i, vertices));
		return p;
	}

	/**
	 * Decompose the polygon into several smaller non-concave polygon.
	 * If the polygon is already convex, it will return the original polygon,
	 * unless it is over Settings.MaxPolygonVertices.
	 * Precondition: Counter Clockwise polygon
	 */
	public static Array<Array<Vector2>> convexPartition(Array<Vector2> vertices) {
		// We force it to CCW as it is a precondition in this algorithm.
		// vertices.ForceCounterClockWise();
		if(areVerticesClockwise(toFloatArray((Vector2[]) vertices.toArray(Vector2.class))))
			vertices.reverse();
		Array<Array<Vector2>> list = new Array<Array<Vector2>>();
		float d, lowerDist, upperDist;
		Vector2 p;
		Vector2 lowerInt = new Vector2();
		Vector2 upperInt = new Vector2(); // intersection points
		int lowerIndex = 0, upperIndex = 0;
		Array<Vector2> lowerPoly, upperPoly;
		for(int i = 0; i < vertices.size; ++i) {
			if(reflex(i, vertices)) {
				lowerDist = upperDist = Float.MAX_VALUE; // std::numeric_limits<qreal>::max();
				for(int j = 0; j < vertices.size; ++j) {
					// if line intersects with an edge
					if(left(at(i - 1, vertices), at(i, vertices), at(j, vertices)) && rightOn(at(i - 1, vertices), at(i, vertices), at(j - 1, vertices))) {
						// find the point of intersection
						p = lineIntersect(at(i - 1, vertices), at(i, vertices), at(j, vertices), at(j - 1, vertices));
						if(right(at(i + 1, vertices), at(i, vertices), p)) {
							// make sure it's inside the poly
							d = squareDist(at(i, vertices), p);
							if(d < lowerDist) {
								// keep only the closest intersection
								lowerDist = d;
								lowerInt = p;
								lowerIndex = j;
							}
						}
					}
					if(left(at(i + 1, vertices), at(i, vertices), at(j + 1, vertices)) && rightOn(at(i + 1, vertices), at(i, vertices), at(j, vertices))) {
						p = lineIntersect(at(i + 1, vertices), at(i, vertices), at(j, vertices), at(j + 1, vertices));
						if(left(at(i - 1, vertices), at(i, vertices), p)) {
							d = squareDist(at(i, vertices), p);
							if(d < upperDist) {
								upperDist = d;
								upperIndex = j;
								upperInt = p;
							}
						}
					}
				}
				// if there are no vertices to connect to, choose a point in the
				// middle
				if(lowerIndex == (upperIndex + 1) % vertices.size) {
					Vector2 sp = new Vector2((lowerInt.x + upperInt.x) / 2, (lowerInt.y + upperInt.y) / 2);
					lowerPoly = copy(i, upperIndex, vertices);
					lowerPoly.add(sp);
					upperPoly = copy(lowerIndex, i, vertices);
					upperPoly.add(sp);
				} else {
					double highestScore = 0, bestIndex = lowerIndex;
					while(upperIndex < lowerIndex)
						upperIndex += vertices.size;
					for(int j = lowerIndex; j <= upperIndex; ++j) {
						if(canSee(i, j, vertices)) {
							double score = 1 / (squareDist(at(i, vertices),
									at(j, vertices)) + 1);
							if(reflex(j, vertices)) {
								if(rightOn(at(j - 1, vertices),
										at(j, vertices), at(i, vertices))
										&& leftOn(at(j + 1, vertices),
												at(j, vertices),
												at(i, vertices))) {
									score += 3;
								} else
									score += 2;
							} else
								score += 1;
							if(score > highestScore) {
								bestIndex = j;
								highestScore = score;
							}
						}
					}
					lowerPoly = copy(i, (int) bestIndex, vertices);
					upperPoly = copy((int) bestIndex, i, vertices);
				}
				list.addAll(convexPartition(lowerPoly));
				list.addAll(convexPartition(upperPoly));
				return list;
			}
		}
		// polygon is already convex
		if(vertices.size > maxPolygonVertices) {
			lowerPoly = copy(0, vertices.size / 2, vertices);
			upperPoly = copy(vertices.size / 2, 0, vertices);
			list.addAll(convexPartition(lowerPoly));
			list.addAll(convexPartition(upperPoly));
		} else
			list.add(vertices);
		// The polygons are not guaranteed to be with collinear points. We
		// remove
		// them to be sure.
		for(int i = 0; i < list.size; i++)
			list.set(i, SimplifyTools.collinearSimplify(list.get(i), 0));
		// Remove empty vertice collections
		for(int i = list.size - 1; i >= 0; i--)
			if(list.get(i).size == 0)
				list.removeIndex(i);
		return list;
	}

	private static boolean canSee(int i, int j, Array<Vector2> vertices) {
		if(reflex(i, vertices)) {
			if(leftOn(at(i, vertices), at(i - 1, vertices), at(j, vertices)) && rightOn(at(i, vertices), at(i + 1, vertices), at(j, vertices)))
				return false;
		} else {
			if(rightOn(at(i, vertices), at(i + 1, vertices), at(j, vertices)) || leftOn(at(i, vertices), at(i - 1, vertices), at(j, vertices)))
				return false;
		}
		if(reflex(j, vertices)) {
			if(leftOn(at(j, vertices), at(j - 1, vertices), at(i, vertices)) && rightOn(at(j, vertices), at(j + 1, vertices), at(i, vertices)))
				return false;
		} else {
			if(rightOn(at(j, vertices), at(j + 1, vertices), at(i, vertices)) || leftOn(at(j, vertices), at(j - 1, vertices), at(i, vertices)))
				return false;
		}
		for(int k = 0; k < vertices.size; ++k) {
			if((k + 1) % vertices.size == i || k == i || (k + 1) % vertices.size == j || k == j)
				continue; // ignore incident edges
			Vector2 intersectionPoint = new Vector2();
			if(lineIntersect(at(i, vertices), at(j, vertices), at(k, vertices), at(k + 1, vertices), true, true, intersectionPoint))
				return false;
		}
		return true;
	}

	public static Vector2 lineIntersect(Vector2 p1, Vector2 p2, Vector2 q1, Vector2 q2) {
		Vector2 i = new Vector2();
		float a1 = p2.y - p1.y;
		float b1 = p1.x - p2.x;
		float c1 = a1 * p1.x + b1 * p1.y;
		float a2 = q2.y - q1.y;
		float b2 = q1.x - q2.x;
		float c2 = a2 * q1.x + b2 * q1.y;
		float det = a1 * b2 - a2 * b1;
		if(!floatEquals(det, 0)) {
			// lines are not parallel
			i.x = (b2 * c1 - b1 * c2) / det;
			i.y = (a1 * c2 - a2 * c1) / det;
		}
		return i;
	}

	public static boolean floatEquals(float value1, float value2) {
		return Math.abs(value1 - value2) <= EPSILON;
	}

	/**
	 * This method detects if two line segments (or lines) intersect,
	 * and, if so, the point of intersection. Use the firstIsSegment and
	 * secondIsSegment parameters to set whether the
	 * intersection point
	 * must be on the first and second line segments. Setting these
	 * both to true means you are doing a line-segment to line-segment
	 * intersection. Setting one of them to true means you are doing a
	 * line to line-segment intersection test, and so on.
	 * Note: If two line segments are coincident, then
	 * no intersection is detected (there are actually
	 * infinite intersection points).
	 *
	 * @param point1 The first point of the first line segment.
	 * @param point2 The second point of the first line segment.
	 * @param point3 The first point of the second line segment.
	 * @param point4 The second point of the second line segment.
	 * @param firstIsSegment Set this to true to require that the intersection point be on the first line segment.
	 * @param secondIsSegment Set this to true to require that the intersection point be on the second line segment.
	 * @param point This is set to the intersection point if an intersection is detected.
	 * @return True if an intersection is detected, false otherwise.
	 * 
	 * Author: Jeremy Bell
	 */
	public static boolean lineIntersect(Vector2 point1, Vector2 point2, Vector2 point3, Vector2 point4, boolean firstIsSegment, boolean secondIsSegment, Vector2 point) {
		point = new Vector2();
		// these are reused later.
		// each lettered sub-calculation is used twice, except
		// for b and d, which are used 3 times
		float a = point4.y - point3.y;
		float b = point2.x - point1.x;
		float c = point4.x - point3.x;
		float d = point2.y - point1.y;
		// denominator to solution of linear system
		float denom = a * b - c * d;
		// if denominator is 0, then lines are parallel
		if(!(denom >= -EPSILON && denom <= EPSILON)) {
			float e = point1.y - point3.y;
			float f = point1.x - point3.x;
			float oneOverDenom = 1.0f / denom;
			// numerator of first equation
			float ua = c * e - a * f;
			ua *= oneOverDenom;
			// check if intersection point of the two lines is on line segment 1
			if(!firstIsSegment || ua >= 0.0f && ua <= 1.0f) {
				// numerator of second equation
				float ub = b * e - d * f;
				ub *= oneOverDenom;
				// check if intersection point of the two lines is on line
				// segment 2
				// means the line segments intersect, since we know it is on
				// segment 1 as well.
				if(!secondIsSegment || ub >= 0.0f && ub <= 1.0f) {
					// check if they are coincident (no collision in this case)
					if(ua != 0f || ub != 0f) {
						// There is an intersection
						point.x = point1.x + ua * b;
						point.y = point1.y + ua * d;
						return true;
					}
				}
			}
		}
		return false;
	}

	// precondition: ccw
	private static boolean reflex(int i, Array<Vector2> vertices) {
		return right(i, vertices);
	}

	private static boolean right(int i, Array<Vector2> vertices) {
		return right(at(i - 1, vertices), at(i, vertices), at(i + 1, vertices));
	}

	private static boolean left(Vector2 a, Vector2 b, Vector2 c) {
		return area(a, b, c) > 0;
	}

	private static boolean leftOn(Vector2 a, Vector2 b, Vector2 c) {
		return area(a, b, c) >= 0;
	}

	private static boolean right(Vector2 a, Vector2 b, Vector2 c) {
		return area(a, b, c) < 0;
	}

	private static boolean rightOn(Vector2 a, Vector2 b, Vector2 c) {
		return area(a, b, c) <= 0;
	}

	public static float area(Vector2 a, Vector2 b, Vector2 c) {
		return a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y);
	}

	private static float squareDist(Vector2 a, Vector2 b) {
		float dx = b.x - a.x;
		float dy = b.y - a.y;
		return dx * dx + dy * dy;
	}

	public static class SimplifyTools {

		private static boolean[] usePt;
		private static double distanceTolerance;

		/**
		 * Removes all collinear points on the polygon.
		 * @param vertices The polygon that needs simplification.
		 * @param collinearityTolerance The collinearity tolerance.
		 * @return A simplified polygon.
		 */
		public static Array<Vector2> collinearSimplify(Array<Vector2> vertices, float collinearityTolerance) {
			// We can't simplify polygons under 3 vertices
			if(vertices.size < 3)
				return vertices;
			Array<Vector2> simplified = new Array<Vector2>();
			for(int i = 0; i < vertices.size; i++) {
				int prevId = i - 1;
				if(prevId < 0)
					prevId = vertices.size - 1;
				int nextId = i + 1;
				if(nextId >= vertices.size)
					nextId = 0;
				Vector2 prev = vertices.get(prevId);
				Vector2 current = vertices.get(i);
				Vector2 next = vertices.get(nextId);
				// If they collinear, continue
				if(collinear(prev, current, next, collinearityTolerance))
					continue;
				simplified.add(current);
			}
			return simplified;
		}

		public static boolean collinear(Vector2 a, Vector2 b, Vector2 c, float tolerance) {
			return floatInRange(BayazitDecomposer.area(a, b, c), -tolerance, tolerance);
		}

		public static boolean floatInRange(float value, float min, float max) {
			return value >= min && value <= max;
		}

		/**
		 * Removes all collinear points on the polygon.
		 * Has a default bias of 0
		 * @param vertices The polygon that needs simplification
		 * @return A simplified polygon.
		 */
		public static Array<Vector2> collinearSimplify(Array<Vector2> vertices) {
			return collinearSimplify(vertices, 0);
		}

		/**
		 * Ramer-Douglas-Peucker polygon simplification algorithm. This is the
		 * general recursive version that does not use the
		 * speed-up technique by using the Melkman convex hull.
		 * If you pass in 0, it will remove all collinear points
		 * @param vertices
		 * @param distanceTolerance
		 * @return The simplified polygon
		 */
		public static Array<Vector2> douglasPeuckerSimplify(Array<Vector2> vertices, float distanceTolerance) {
			SimplifyTools.distanceTolerance = distanceTolerance;
			usePt = new boolean[vertices.size];
			for(int i = 0; i < vertices.size; i++)
				usePt[i] = true;
			simplifySection(vertices, 0, vertices.size - 1);
			Array<Vector2> result = new Array<Vector2>();
			for(int i = 0; i < vertices.size; i++)
				if(usePt[i])
					result.add(vertices.get(i));
			return result;
		}

		private static void simplifySection(Array<Vector2> vertices, int i, int j) {
			if(i + 1 == j)
				return;
			Vector2 A = vertices.get(i);
			Vector2 B = vertices.get(j);
			double maxDistance = -1.0;
			int maxIndex = i;
			for(int k = i + 1; k < j; k++) {
				double distance = distancePointLine(vertices.get(k), A, B);
				if(distance > maxDistance) {
					maxDistance = distance;
					maxIndex = k;
				}
			}
			if(maxDistance <= distanceTolerance)
				for(int k = i + 1; k < j; k++)
					usePt[k] = false;
			else {
				simplifySection(vertices, i, maxIndex);
				simplifySection(vertices, maxIndex, j);
			}
		}

		private static double distancePointPoint(Vector2 p, Vector2 p2) {
			double dx = p.x - p2.x;
			double dy = p.y - p2.x;
			return Math.sqrt(dx * dx + dy * dy);
		}

		private static double distancePointLine(Vector2 p, Vector2 A, Vector2 B) {
			// if start == end, then use point-to-point distance
			if(A.x == B.x && A.y == B.y)
				return distancePointPoint(p, A);
			// otherwise use comp.graphics.algorithms Frequently Asked Questions
			// method
			/*
			 * (1) AC dot AB r = --------- ||AB||^2 r has the following meaning: r=0
			 * Point = A r=1 Point = B r<0 Point is on the backward extension of AB
			 * r>1 Point is on the forward extension of AB 0<r<1 Point is interior
			 * to AB
			 */
			double r = ((p.x - A.x) * (B.x - A.x) + (p.y - A.y) * (B.y - A.y))
					/ ((B.x - A.x) * (B.x - A.x) + (B.y - A.y) * (B.y - A.y));
			if(r <= 0.0)
				return distancePointPoint(p, A);
			if(r >= 1.0)
				return distancePointPoint(p, B);
			/*
			 * (2) (Ay-Cy)(Bx-Ax)-(Ax-Cx)(By-Ay) s = -----------------------------
			 * Curve^2 Then the distance from C to Point = |s|*Curve.
			 */
			double s = ((A.y - p.y) * (B.x - A.x) - (A.x - p.x) * (B.y - A.y))
					/ ((B.x - A.x) * (B.x - A.x) + (B.y - A.y) * (B.y - A.y));
			return Math.abs(s)
					* Math.sqrt((B.x - A.x) * (B.x - A.x) + (B.y - A.y)
							* (B.y - A.y));
		}

		// From physics2d.net
		public static Array<Vector2> reduceByArea(Array<Vector2> vertices, float areaTolerance) {
			if(vertices.size <= 3)
				return vertices;
			if(areaTolerance < 0)
				throw new IllegalArgumentException("areaTolerance: must be equal to or greater then zero.");
			Array<Vector2> result = new Array<Vector2>();
			Vector2 v1, v2, v3;
			float old1, old2, new1;
			v1 = vertices.get(vertices.size - 2);
			v2 = vertices.get(vertices.size - 1);
			areaTolerance *= 2;
			for(int index = 0; index < vertices.size; ++index, v2 = v3) {
				if(index == vertices.size - 1) {
					if(result.size == 0)
						throw new IllegalArgumentException("areaTolerance: The tolerance is too high!");
					v3 = result.get(0);
				} else
					v3 = vertices.get(index);
				old1 = cross(v1, v2);
				old2 = cross(v2, v3);
				new1 = cross(v1, v3);
				if(Math.abs(new1 - (old1 + old2)) > areaTolerance) {
					result.add(v2);
					v1 = v2;
				}
			}
			return result;
		}

		public static Float cross(Vector2 a, Vector2 b) {
			return a.x * b.y - a.y * b.x;
		}

		/** From Eric Jordan's convex decomposition library
		 *  Merges all parallel edges in the list of vertices */
		public static void mergeParallelEdges(Array<Vector2> vertices, float tolerance) {
			if(vertices.size <= 3)
				return; // Can't do anything useful here to a triangle
			boolean[] mergeMe = new boolean[vertices.size];
			int newNVertices = vertices.size;
			// Gather points to process
			for(int i = 0; i < vertices.size; ++i) {
				int lower = i == 0 ? vertices.size - 1 : i - 1;
				int middle = i;
				int upper = i == vertices.size - 1 ? 0 : i + 1;
				float dx0 = vertices.get(middle).x - vertices.get(lower).x;
				float dy0 = vertices.get(middle).y - vertices.get(lower).y;
				float dx1 = vertices.get(upper).y - vertices.get(middle).x;
				float dy1 = vertices.get(upper).y - vertices.get(middle).y;
				float norm0 = (float) Math.sqrt(dx0 * dx0 + dy0 * dy0);
				float norm1 = (float) Math.sqrt(dx1 * dx1 + dy1 * dy1);
				if(!(norm0 > 0.0f && norm1 > 0.0f) && newNVertices > 3) {
					// Merge identical points
					mergeMe[i] = true;
					--newNVertices;
				}
				dx0 /= norm0;
				dy0 /= norm0;
				dx1 /= norm1;
				dy1 /= norm1;
				float cross = dx0 * dy1 - dx1 * dy0;
				float dot = dx0 * dx1 + dy0 * dy1;
				if(Math.abs(cross) < tolerance && dot > 0 && newNVertices > 3) {
					mergeMe[i] = true;
					--newNVertices;
				} else
					mergeMe[i] = false;
			}
			if(newNVertices == vertices.size || newNVertices == 0)
				return;
			int currIndex = 0;
			// Copy the vertices to a new list and clear the old
			Array<Vector2> oldVertices = new Array<Vector2>(vertices);
			vertices.clear();
			for(int i = 0; i < oldVertices.size; ++i) {
				if(mergeMe[i] || newNVertices == 0 || currIndex == newNVertices)
					continue;
				// Debug.Assert(currIndex < newNVertices);
				vertices.add(oldVertices.get(i));
				++currIndex;
			}
		}

		/** Merges the identical points in the polygon. */
		public static Array<Vector2> mergeIdenticalPoints(Array<Vector2> vertices) {
			Array<Vector2> results = new Array<Vector2>();
			for(int i = 0; i < vertices.size; i++) {
				Vector2 vOriginal = vertices.get(i);

				boolean alreadyExists = false;
				for(int j = 0; j < results.size; j++) {
					Vector2 v = results.get(j);
					if(vOriginal.equals(v)) {
						alreadyExists = true;
						break;
					}
				}
				if(!alreadyExists)
					results.add(vertices.get(i));
			}
			return results;
		}

		/**
		 * Reduces the polygon by distance.
		 * @param vertices
		 * @param distance The distance between points. Points closer than this will be 'joined'
		 */
		public static Array<Vector2> reduceByDistance(Array<Vector2> vertices, float distance) {
			// We can't simplify polygons under 3 vertices
			if(vertices.size < 3)
				return vertices;
			Array<Vector2> simplified = new Array<Vector2>();
			for(int i = 0; i < vertices.size; i++) {
				Vector2 current = vertices.get(i);
				int ii = i + 1;
				if(ii >= vertices.size)
					ii = 0;
				Vector2 next = vertices.get(ii);
				Vector2 diff = new Vector2(next.x - current.x, next.y - current.y);
				// If they are closer than the distance, continue
				if(diff.len2() <= distance)
					continue;
				simplified.add(current);
			}
			return simplified;
		}

		/**
		 * Reduces the polygon by removing the Nth vertex in the vertices list.
		 * @param vertices
		 * @param nth The Nth point to remove. Example: 5.
		 */
		public static Array<Vector2> reduceByNth(Array<Vector2> vertices, int nth) {
			// We can't simplify polygons under 3 vertices
			if(vertices.size < 3)
				return vertices;
			if(nth == 0)
				return vertices;
			Array<Vector2> result = new Array<Vector2>(vertices.size);
			for(int i = 0; i < vertices.size; i++) {
				if(i % nth == 0)
					continue;
				result.add(vertices.get(i));
			}
			return result;
		}
	}

}
