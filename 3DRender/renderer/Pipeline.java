package code.renderer;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;


/**
 * The Pipeline class has method stubs for all the major components of the
 * rendering pipeline, for you to fill in.
 * 
 * Some of these methods can get quite long, in which case you should strongly
 * consider moving them out into their own file. You'll need to update the
 * imports in the test suite if you do.
 */
public class Pipeline {

	/**
	 * Returns true if the given polygon is facing away from the camera (and so
	 * should be hidden), and false otherwise.
	 */
	public static boolean isHidden(Polygon poly) {
		return poly.getNormal().z > 1e-5;
	}

	/**
	 * Computes the colour of a polygon on the screen, once the lights, their
	 * angles relative to the polygon's face, and the reflectance of the polygon
	 * have been accounted for.
	 * 
	 * @param lightDirection
	 *            The Vector3D pointing to the directional light read in from
	 *            the file.
	 * @param lightColor
	 *            The color of that directional light.
	 * @param ambientLight
	 *            The ambient light in the scene, i.e. light that doesn't depend
	 *            on the direction.
	 */
	public static Color getShading(Polygon poly, Vector3D lightDirection, Color lightColor, Color ambientLight) {

		Vector3D normal = poly.getNormal();
		double cos = normal.cosTheta(lightDirection);

		int red,green,blue;

		// (ambient light color + light color*dotProduct)* reflection
		if(cos>0){

			red = (int)(poly.reflectance.getRed() / 255.0f * (ambientLight.getRed() + lightColor.getRed() * cos));
			green = (int)(poly.reflectance.getGreen() / 255.0f*(ambientLight.getGreen() + lightColor.getGreen()*cos));
			blue = (int)(poly.reflectance.getBlue() / 255.0f * (ambientLight.getBlue() + lightColor.getBlue()*cos));

		}else{
			red = (int)(poly.reflectance.getRed() / 255.0f*ambientLight.getRed());
			green = (int)(poly.reflectance.getGreen() / 255.0f*ambientLight.getGreen());
			blue = (int)(poly.reflectance.getBlue() / 255.0f*ambientLight.getBlue());
		}

		red = red > 255 ? 255 : red;
		green = green > 255 ? 255 : green;
		blue = blue > 255 ? 255 : blue;
		return new Color(red,green,blue);

	}

	/**
	 * This method should rotate the polygons and light such that the viewer is
	 * looking down the Z-axis. The idea is that it returns an entirely new
	 * Scene object, filled with new Polygons, that have been rotated.
	 * 
	 * @param scene
	 *            The original Scene.
	 * @param xRot
	 *            An angle describing the viewer's rotation in the YZ-plane (i.e
	 *            around the X-axis).
	 * @param yRot
	 *            An angle describing the viewer's rotation in the XZ-plane (i.e
	 *            around the Y-axis).
	 * @return A new Scene where all the polygons and the light source have been
	 *         rotated accordingly.
	 */
	public static Scene rotateScene(Scene scene, float xRot, float yRot) {

		//the Rotation matrix
		Transform rotationMatrix = Transform.newXRotation(xRot).compose(Transform.newYRotation(yRot));

		return processMatrix(scene,rotationMatrix);

	}

	/**
	 * This should translate the scene by the appropriate amount.
	 * 
	 * @param scene
	 * @param z 
	 * @param y 
	 * @param x 
	 * @return
	 */
	public static Scene translateScene(Scene scene, float offset_x, float offset_y, float offset_z) {
		//the translation matrix
		Transform translationMatrix = Transform.newTranslation(offset_x,offset_y,offset_z);
		return processMatrix(scene, translationMatrix);
	}

	/**
	 * This should scale the scene.
	 * 
	 * @param scene
	 * @return
	 */
	public static Scene scaleScene(Scene scene,float offset_x, float offset_y,float offset_z ) {
		//the scale matrix
		Transform scaleMatrix = Transform.newScale(offset_x,offset_y,offset_z);
		return processMatrix(scene, scaleMatrix);

	}
	/**
	 * This method process a scene (polygons and light) with a given matrix.
	 * 
	 * @param scene
	 * @param matrix   --the given matrix, can be a rotation, translation, or a scaling matrix
	 * 
	 * @return a Scene object
	 * */
	private static Scene processMatrix(Scene scene, Transform scaleMatrix) {
		//process light
		Vector3D processedLightPos = scaleMatrix.multiply(scene.getLight());
		//process polygons
		List<Polygon> processedPolygonList = new ArrayList<>();
		for (Polygon polygon : scene.getPolygons()) {
			Vector3D[] processedVectors = new Vector3D[3];
			for (int i = 0; i < processedVectors.length; i++) {
				processedVectors[i] = scaleMatrix.multiply(polygon.vertices[i]);
			}
			Polygon processedPolygon = new Polygon(processedVectors[0], processedVectors[1],processedVectors[2],polygon.reflectance);
			processedPolygonList.add(processedPolygon);
		}
		
		return new Scene(processedPolygonList, processedLightPos);

	}

	

	
	
	
	/**
	 * Computes the edgelist of a single provided polygon, as per the lecture
	 * slides.
	 */
	public static EdgeList computeEdgeList(Polygon poly) {
		Vector3D vertex1 = poly.vertices[0];
		Vector3D vertex2 = poly.vertices[1];
		Vector3D vertex3 = poly.vertices[2];

		int startY = (int) Math.min(Math.min(vertex1.y,vertex2.y),vertex3.y);
		int endY = (int) Math.max(Math.max(vertex1.y,vertex2.y),vertex3.y);

		EdgeList edgeList = new EdgeList(startY,endY);

		for(int i = 0; i < poly.vertices.length;i++){
			int j = i + 1;
			j = j == 3 ? 0 : j; //to prevent index out of boundary exception
			Vector3D vertexUp, vertexDown;

			if(poly.vertices[i].y == poly.vertices[j].y){
				continue; //these two vertices have same y value
			} else if (poly.vertices[i].y < poly.vertices[j].y){
				vertexUp = poly.vertices[i];
				vertexDown = poly.vertices[j];
			} else{
				vertexDown = poly.vertices[i];
				vertexUp = poly.vertices[j];
			}

			float x = vertexUp.x;
			float z = vertexUp.z;

			float mx = (vertexDown.x - x) / (vertexDown.y -vertexUp.y);
			float mz = (vertexDown.z - z) / (vertexDown.y - vertexUp.y);

			int y = (int) vertexUp.y;
			int yEnd = (int) vertexDown.y;

			while(y < yEnd){
				edgeList.addRow(y-startY,x,z);
				y++;
				x += mx;
				z += mz;
			}


		}
		return edgeList;
	}

	/**
	 * Fills a zbuffer with the contents of a single edge list according to the
	 * lecture slides.
	 * 
	 * The idea here is to make zbuffer and zdepth arrays in your main loop, and
	 * pass them into the method to be modified.
	 * 
	 * @param zbuffer
	 *            A double array of colours representing the Color at each pixel
	 *            so far.
	 * @param zdepth
	 *            A double array of floats storing the z-value of each pixel
	 *            that has been coloured in so far.
	 * @param polyEdgeList
	 *            The edgelist of the polygon to add into the zbuffer.
	 * @param polyColor
	 *            The colour of the polygon to add into the zbuffer.
	 */
	public static void computeZBuffer(Color[][] zbuffer, float[][] zdepth, EdgeList polyEdgeList, Color polyColor) {
		int startY = polyEdgeList.getStartY();
		int endY = polyEdgeList.getStartY();
		int height = endY - startY;
		int y = 0;

		while ( y < height){
			// do not render pixels that are out-of-boundary
			if( y+startY < 0 || y+startY >= zbuffer[0].length){
				y++;
				continue;
			}

			int x = (int) polyEdgeList.getXLeft(y);
			int xRight = (int) polyEdgeList.getXRight(y);
			float z = polyEdgeList.getZLeft(y);
			float zRight = polyEdgeList.getZRight(y);
			float mz =( zRight -z) / (xRight - x);

			while(x < xRight){
			    //do not render pixels that are out-of-boundary
                if(x<0 || x>=zbuffer.length){
                    z += mz;
                    x++;
                    continue;
                }
                if(z < zdepth[x][y + startY]){
                    zdepth[x][y + startY] = z;
                    zbuffer[x][y + startY] = polyColor;

                }
                z += mz;
                x++;

            }

			y++;

		}

	}

	public static Scene fitByScaleTranslate(Scene scene, float[] boundary, Dimension dimension) {

	    float left = boundary[0];
		float right = boundary[1];
		float up = boundary[2];
		float down = boundary[3];
		float close = boundary[4];
		float far = boundary[5];

		float objectWidth = right - left;
		float objectHeight = down - up;
		float objectDepth = far - close;
		int canvasWidth = dimension.width;
		int canvasHeight = dimension.height;
        // scale;
		float ratioHorizontal = canvasWidth / 2 /  objectWidth;
		float ratioVertical = canvasHeight / 2 / objectHeight;
		float ratioDepth = Math.min(canvasWidth, canvasHeight) / 2 / objectDepth;

		float scale = Math.min(Math.min(ratioHorizontal,ratioVertical),ratioDepth);
		Transform scaleMatrix = Transform.newScale(scale,scale,scale);
		Scene scaledScene = processMatrix(scene,scaleMatrix);

		//Auto-translate
        float scaledLeft = left * scale;
        float scaledUp = up * scale;

        //how far it will shift horizontally
        float scaledObjectWidth = objectWidth * scale;
        float centralPosX = (canvasWidth - scaledObjectWidth) / 2;
        float shift_horizontal =centralPosX -scaledLeft;
        //how far it will shift vertically
        float scaledObjectHeight = objectHeight * scale;
        float centralPosY = (canvasHeight - scaledObjectHeight) / 2;
        float shift_vertial = centralPosY - scaledUp;

        Transform translationMatrix = Transform.newTranslation(shift_horizontal,shift_vertial,0f);
        return processMatrix(scaledScene,translationMatrix);

	}

	public static Scene reTranslation(Scene scaledScene, float[] newBoundary, Dimension dimension) {
		
		float left = newBoundary[0];
		float right = newBoundary[1];
		float top = newBoundary[2];
		float bottom = newBoundary[3];
		
		float objectWidth = right -left;
		float objectHeight = bottom - top;
		
		//how much remove horizontally
		float centralPosX = (dimension.width - objectWidth) /2;
		float shiftingHorizontally =centralPosX - left;
		//how much remove vertically
		float centralPosY = (dimension.height - objectHeight)/2;
		float shiftVertically = centralPosY - top;
		
		Transform translationMatrix = Transform.newTranslation(shiftingHorizontally,shiftVertically,0f);
		return processMatrix(scaledScene, translationMatrix);
		
	}
}

// code for comp261 assignments
