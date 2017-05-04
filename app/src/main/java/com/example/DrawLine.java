
package com.example;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import quaternions.Quaternion;


public class DrawLine implements Renderer
{
	
	float x=.1f,y=.1f,z=.1f;
	//private float r=4253;

	private float xDegree=0.1f,yDegree=0.1f,zDegree=0.1f;

	Handler handler,handler2;
	private Timer timer = new Timer();
	private TimerTask task;

	// ����Open GL ES��������Ҫ��Buffer����
	FloatBuffer lineVerticesBuffer;   //����
	FloatBuffer xyzVerticesBuffer;
	ByteBuffer lineFacetsBuffer;	 	 //��
	ByteBuffer xiangliangFacetsBuffer;
	ByteBuffer XFacetsBuffer;
	ByteBuffer YFacetsBuffer;
	ByteBuffer ZFacetsBuffer;

	void updateXYZ(){

		// �����������8������
		float[] lineVertices = new float[] {
			// �϶��������ε��ĸ�����
			x, y, z,//0
			x, 0,z,//1
			0,0,z,//2
			0,y,z,//3

			// �µ��������ε��ĸ�����
			x,y,0,//4
			x,0,0,//5
			0,0,0,//6ԭ��
			0,y,0,//7

		};
		//����XYZ�������ʾ����
		float xyzVertices[]=new float[]{
			-1.2f ,0f, 0f,//0 x��㣬���������

			1.2f ,0f, 0f,//1 X����յ�
			1.0f,0.1f,0f,//2 X���ͷ1
			1.0f,-0.1f,0f,//3 X���ͷ2

			0f ,-1.2f , 0f,//4 Y�����
			0f ,1.2f , 0f,//5 Y���յ�
			0.1f ,1.0f ,0f,//6 Y���ͷ1
			-0.1f ,1.0f ,0f,//7 Y���ͷ2

			0f ,0f ,-1.2f,//8 Z�����
			0f ,0f ,1.2f,//9 Z���յ�
			0f ,-0.1f ,1.0f,//10 Z���ͷ1
			0f ,0.1f ,1.0f,//11 Z���ͷ2

			1.3f,0f,0f,//12 ������X
			1.35f,0.1f,0f,//13
			1.25f,0.1f,0f,//14
			1.25f,-0.1f,0f,//15
			1.35f,-0.1f,0f,//16

			0f,1.4f,0f,//17 ������Y
			0f,1.3f,0f,//18
			0.05f,1.5f,0f,//19
			-0.05f,1.5f,0f,//20

			-0.05f ,0.05f ,1.25f,//21  ������Z
			0.05f,0.05f,1.25f,//22
			-0.05f,-0.05f,1.25f,//23
			0.05f,-0.05f,1.25f,//24

			//�̶�X��̶�
			0.6f,0f,0f,//25
			0.6f,0.1f,0f,//26
			-0.6f,0f,0f,//27
			-0.6f,0.1f,0f,//28
			//�̶�y��̶�
			0f,0.6f,0f,//29
			-0.1f,0.6f,0f,//30
			0f,-0.6f,0f,//31
			-0.1f,-0.6f,0f,//32
			//�̶�Z��̶�
			0f,0f,0.6f,//33
			0f,0.1f,0.6f,//34
			0f,0f,-0.6f,//35
			0f,0.1f,-0.6f//36

		};
		//���������12����
		 byte[] lineFacets = new byte[]{
				0,1,   //������
				0,3,
				0,4,
				1,2,
				1,5,
				2,3,
				2,6,
				3,7,
				4,5,
				4,7,
				5,6,
				6,7
		};
		//������ԭ��6ָ�򳤷����0��
		 byte[] xiangliangFacets = new byte[] {
				6,0//6,0
		};

		//X���꼰���ͷ
		 byte[] XFacets = new byte[] {
				//���յ�
				0,1,
				//��ͷ
				1,2,
				1,3,
				//X
				12,13,
				12,14,
				12,15,
				12,16,
				//X����
				25,26,
				27,28

		};
		//Y���꼰���ͷ
		 byte[] YFacets = new byte[] {
				//���յ�
				4,5,
				//��ͷ
				5,6,
				5,7,
				//��Y
				17,18,
				17,19,
				17,20,
				//Y��̶�
				29,30,
				31,32

		};
		//Z���꼰���ͷ
		 byte[] ZFacets = new byte[] {
				//���յ�
				8,9,
				//��ͷ
				9,10,
				9,11,
				//��Z
				21,22,
				22,23,
				23,24,
				//Z��̶�
				33,34,
				35,36
		};
		// ��������Ķ���λ�����������װ��FloatBuffer;
		lineVerticesBuffer = floatBufferUtil(lineVertices);
		xyzVerticesBuffer = floatBufferUtil(xyzVertices);
		// ��ֱ�ߵ������װ��ByteBuffer
		lineFacetsBuffer = ByteBuffer.wrap(lineFacets);
		xiangliangFacetsBuffer = ByteBuffer.wrap(xiangliangFacets);
		XFacetsBuffer = ByteBuffer.wrap(XFacets);
		YFacetsBuffer = ByteBuffer.wrap(YFacets);
		ZFacetsBuffer = ByteBuffer.wrap(ZFacets);
	}
	//���캯�����˲���������Ϊ��ͬ��䴫�ݲ����������ܷ���handlerʵ�����ݹ���
	public DrawLine(Handler handler_zjk) {

		handler2=handler_zjk;//ʵ����handler2���ǽ��շ���ʵ���ˣ������ת������ʵ�ֲ�ͬ���handler��������
		handler = new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what==200)//���ǽ��ձ����ж�ʱ�����͹������ź���������������
				updateXYZ();
		    }

		};
		//��ʱ�������з����������źţ��������з�����һ������activity���з���һ��
		task = new TimerTask(){

		   public void run() {
			   String[] xyz = new String[3];//���͸�activity�õ�
			 //2s����xyz�������
/*				x=(float) (Math.random()*(-2)+1);			//[Math.random()����0.0 ��С�� 1.0 ]
				y=(float) (Math.random()*(-2)+1);
				z=(float) (Math.random()*(-2)+1);*/

				//�趨һ��Ҫ��ʾ��XYZλ��,������������ʾС�������λ
				if(x>0)
					xyz[0]=String.valueOf(x).substring(0, 4);
				else
					xyz[0]=String.valueOf(x).substring(0, 5);
				if(y>0)
					xyz[1]=String.valueOf(y).substring(0, 4);
				else
					xyz[1]=String.valueOf(y).substring(0, 5);
				if(z>0)
					xyz[2]=String.valueOf(z).substring(0, 4);
				else
					xyz[2]=String.valueOf(z).substring(0, 5);



				Message msg = new Message();
				msg.what=200;//���Ƿ��͸���ǰ�������������������
				handler.sendEmptyMessage(msg.what);

				msg.what=0x123;//���Ƿ��͸�activity�������������ı�����XYZֵ��
				Bundle bundle = new Bundle();//�����ݰ�װ����MSG����
				//Ҫ���͵���һ���ַ������飬��һ��������ָ�����ݵ���������������ʱ�������������ѡ���ȡ�ĸ����ݣ��ܷ��㣬�ڶ��������Ƿ��͵�����
				bundle.putStringArray("xyz", xyz);
				msg.setData(bundle);
				handler2.sendMessage(msg);//handler2ָ���ǽ��մ���Handlerʵ����������Ϊ����ͬһ�࣬����Ҫ�ù��캯�����������handlerʵ�����ݹ���


		   }
	   };
	   timer.schedule(task, 0, 1000);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		// �رտ�����
		gl.glDisable(GL10.GL_DITHER);
		// ����ϵͳ��͸�ӽ�������
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glClearColor(0, 0, 0, 0);
		// ������Ӱƽ��ģʽ
		gl.glShadeModel(GL10.GL_SMOOTH);
		// ������Ȳ���
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// ������Ȳ��Ե�����
		gl.glDepthFunc(GL10.GL_LEQUAL);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		// ����3D�Ӵ��Ĵ�С��λ��
		gl.glViewport(0, 0, width, height);
		// ����ǰ����ģʽ��ΪͶӰ����
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// ��ʼ����λ����
		gl.glLoadIdentity();
		// ����͸���Ӵ��Ŀ�ȡ��߶ȱ�
		float ratio = (float) width / height;
		// ���ô˷�������͸���Ӵ��Ŀռ��С��
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
	}

	// ����ͼ�εķ���
	@Override
	public void onDrawFrame(GL10 gl)
	{
		// �����Ļ�������Ȼ���
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// ���ö�����������
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// ���ö�����ɫ����
		//gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		// ���õ�ǰ����ģʽΪģ����ͼ��
		gl.glMatrixMode(GL10.GL_MODELVIEW);

		// --------------------����������---------------------
		// ���õ�ǰ��ģ����ͼ����
		gl.glLoadIdentity();


		gl.glTranslatef(0.0f, -0.0f, -3.0f);//�ƶ�����
		// ����Y����ת
		gl.glRotatef(yDegree,0f,0.1f,.0f);
		//	r++;
		// ����X����ת
		gl.glRotatef(xDegree,0.1f,0f,0f);
		gl.glRotatef(zDegree,0f,0f,0.1f);
		// ����Z����ת




		gl.glLineWidth(2.0f);
		// ���ö����λ������ ��Ϊ���е����ݶ��ڴ������У����Գ������������ֻҪ������һ�ξͺ�
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineVerticesBuffer);
		// ���ö������ɫ����
		gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		//gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 18);//���ﲻ�ö�ά������ά�Ļ�����ע����GL_LINES��ά�л���
		gl.glDrawElements(GL10.GL_LINES, lineFacetsBuffer.remaining(),
						GL10.GL_UNSIGNED_BYTE, lineFacetsBuffer);
		// --------------------��������---------------------
		//��������
		gl.glLineWidth(6.0f);//ֱ�߿�� 5����������	
		//���������õ��ˣ������õ�����������е�
//		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineVerticesBuffer);//����
		gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);//����
		gl.glDrawElements(GL10.GL_LINES, xiangliangFacetsBuffer.remaining(),
				GL10.GL_UNSIGNED_BYTE, xiangliangFacetsBuffer);//����
		// --------------------����X����---------------------
		//����x����
		gl.glLineWidth(3.0f);//ֱ�߿��
		//����XYZ�Ķ��� ��Ϊ����XYZ�����ݶ��ڴ������У�����XYZ��ֻҪ������һ�ξͺ�
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, xyzVerticesBuffer);
		// ���ö������ɫ����
		gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);//X
		gl.glDrawElements(GL10.GL_LINES, XFacetsBuffer.remaining(),
				GL10.GL_UNSIGNED_BYTE, XFacetsBuffer);//X
		// --------------------����Y����---------------------
		//����Y����
		//���������õ��ˣ������õ�����������е�
//		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineVerticesBufferY);//Y
		// ���ö������ɫ����
		gl.glColor4f(1.0f, 1.0f, 0.0f, 1.0f);//Y
		gl.glDrawElements(GL10.GL_LINES, YFacetsBuffer.remaining(),
				GL10.GL_UNSIGNED_BYTE, YFacetsBuffer);//Y
		// --------------------����Z����---------------------
		//����Z����
		//���������õ��ˣ������õ�����������е�
//		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineVerticesBufferZ);//Y
		// ���ö������ɫ����
		gl.glColor4f(1.0f, 0.0f, 1.0f, 1.0f);//z
		gl.glDrawElements(GL10.GL_LINES, ZFacetsBuffer.remaining(),
				GL10.GL_UNSIGNED_BYTE, ZFacetsBuffer);//Z
		// ���ƽ���
		gl.glFinish();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		// ��ת�Ƕ�����1
		//rotate+=1;
	}
	// ����һ�����߷�������int[]����ת��ΪOpenGL ES�����IntBuffer
//	private IntBuffer intBufferUtil(int[] arr)
//	{
//		IntBuffer mBuffer;
//		// ��ʼ��ByteBuffer������Ϊarr����ĳ���*4����Ϊһ��intռ4���ֽ�
//		ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 4);
//		// ����������nativeOrder
//		qbb.order(ByteOrder.nativeOrder());
//		mBuffer = qbb.asIntBuffer();
//		mBuffer.put(arr);
//		mBuffer.position(0);
//		return mBuffer;
//	}
	// ����һ�����߷�������float[]����ת��ΪOpenGL ES�����FloatBuffer
	private FloatBuffer floatBufferUtil(float[] arr)
	{
		FloatBuffer mBuffer;
		// ��ʼ��ByteBuffer������Ϊarr����ĳ���*4����Ϊһ��intռ4���ֽ�
		ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 4);
		// ����������nativeOrder
		qbb.order(ByteOrder.nativeOrder());
		mBuffer = qbb.asFloatBuffer();
		mBuffer.put(arr);
		mBuffer.position(0);
		return mBuffer;
	}
	public void getXYZ(float[] jiasudu,float[] xyzDegree){
		x=jiasudu[0];
		y=jiasudu[1];
		z=jiasudu[2];
		xDegree=xyzDegree[0];
		yDegree=xyzDegree[1];
		zDegree=xyzDegree[2];
	}
}

