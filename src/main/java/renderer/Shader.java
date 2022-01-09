package renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private int shaderProgramID;
    private String vertexSrc;
    private String fragmentSrc;
    private String filepath;

    public Shader(String filepath){
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            //find first pattern vertex type from shader glsl file
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            //find second pattern fragment type from shader glsl file
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")){
                vertexSrc = splitString[1];
            } else if (firstPattern.equals("fragment")){
                fragmentSrc = splitString[1];
            } else {
                throw new IOException("Unexpected Source string" + firstPattern );
            }

            if (secondPattern.equals("vertex")){
                vertexSrc = splitString[2];
            } else if (secondPattern.equals("fragment")){
                fragmentSrc = splitString[2];
            } else {
                throw new IOException("Unexpected Source string" + secondPattern );
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error could not open file for shader: '" + filepath + "'";
        }

        System.out.println(vertexSrc);
        System.out.println((fragmentSrc));
    }

    public void compile(){
        int vertexID, fragmentID;

        //compile and link shaders
        //load and compile vertex
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        //pass shader source code to GPU
        glShaderSource(vertexID, vertexSrc);

        glCompileShader(vertexID);

        //Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: '"+filepath+"'\n \tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : " ";
        }

        //load and compile vertex
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        //pass shader source code to GPU
        glShaderSource(fragmentID, fragmentSrc);

        glCompileShader(fragmentID);

        //Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: '" + filepath + "'\n \tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : " ";
        }


        //link shaders
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        //check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: '" + filepath +"' \n \tshader linking failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : " ";
        }


    }

    public void use(){
        //bind shader program
        glUseProgram(shaderProgramID);
    }

    public void detach(){
        glUseProgram(0);
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }
}
