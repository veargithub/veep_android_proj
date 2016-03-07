package com.example.ant_test.opengles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;
/**
 * @Title: Square.java
 * @Package com.example.ant_test.opengles
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-4-23 下午5:33:13
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class Square {
	// Our vertices.
	private float vertices[] = {
			-1.0f,  1.0f, 0.0f,  // 0, Top Left
			-1.0f, -1.0f, 0.0f,  // 1, Bottom Left
			1.0f, -1.0f, 0.0f,  // 2, Bottom Right
			1.0f,  1.0f, 0.0f,  // 3, Top Right
	};

	 // The order we like to connect them.
	private short[] indices = { 0, 1, 2, 0, 2, 3 };

	 // Our vertex buffer.
	private FloatBuffer vertexBuffer;
	 // Our index buffer.
	private ShortBuffer indexBuffer;

	public Square() {
		 // a float is 4 bytes, therefore we
		 // multiply the number if
		 // vertices with 4.
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	
		 // short is 2 bytes, therefore we multiply
		 //the number if
		 // vertices with 2.
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}

	 /**
	 * This function draws our square on screen.
	 * @param gl
	 */
	 public void draw(GL10 gl) {
		 // Counter-clockwise winding.
		 gl.glFrontFace(GL10.GL_CCW);//逆时针画
		 // Enable face culling.
		 gl.glEnable(GL10.GL_CULL_FACE);//隐藏不显示的那面
		 // What faces to remove with the face culling.
		 gl.glCullFace(GL10.GL_BACK);//隐藏背面(显示正面)
	
		 // Enabled the vertices buffer for writing
		 //and to be used during
		 // rendering.
		 gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//貌似开启后，glDrawArrays, or glDrawElements 这2个方法才能被调用，并且调完后要disable掉
		 // Specifies the location and data format of
		 //an array of vertex
		 // coordinates to use when rendering.
		 gl.glVertexPointer(3, GL10.GL_FLOAT, 0,
		 vertexBuffer);//设置渲染要用到的顶点数组，第1个参数是每个顶点的坐标数，只能是2，3，4（不知道4个值怎么表示一个点？？？），
		// 第2个参数是顶点数组的类型，第3个参数是stride跨度（不太懂。。）,第4个参数是顶点坐标数组
	
		 gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
		 GL10.GL_UNSIGNED_SHORT, indexBuffer);//第1个参数是mode，表示怎么画，第2个参数是要画的元素个数，
		 //第3个参数是参数类型，仅有byte和short这2种，第4个参数是要画的顶点下标数组
	
		 // Disable the vertices buffer.
		 gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);//关闭操作，对应glEnableClientState方法
		 // Disable face culling.
		 gl.glDisable(GL10.GL_CULL_FACE);//关闭操作，对应glEnable(GL10.GL_CULL_FACE)方法
	 }
}
