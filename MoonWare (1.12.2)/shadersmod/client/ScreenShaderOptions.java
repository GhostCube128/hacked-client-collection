package shadersmod.client;

public class ScreenShaderOptions
{
    private String name;
    private ShaderOption[] shaderOptions;
    private int columns;

    public ScreenShaderOptions(String name, ShaderOption[] shaderOptions, int columns)
    {
        this.name = name;
        this.shaderOptions = shaderOptions;
        this.columns = columns;
    }

    public String getName()
    {
        return name;
    }

    public ShaderOption[] getShaderOptions()
    {
        return shaderOptions;
    }

    public int getColumns()
    {
        return columns;
    }
}