package cz.chaloupek.netbeans.smartcomment.options;

/**
 * Poskytuje krátké ukázkové texty pro živý náhled nastavení.
 *
 * Standardní příklady jsou záměrně krátké. Pro delší nebo zvláštní případy
 * slouží záložka Vlastní kód v panelu nastavení.
 */
public final class PreviewSamples
{
    private PreviewSamples()
    {
    }

    public static String getSingleLineSample(PreviewLanguage language)
    {
        switch (language) {
            case PHP: return "    $this->run();";
            case JAVA: return "    service.run();";
            case JAVASCRIPT: return "    console.log(value);";
            case TYPESCRIPT: return "    const value: number = 10;";
            case C: return "    printf(\"Hello\\n\");";
            case CPP: return "    std::cout << value;";
            case C_HEADER: return "    void process(void);";
            case CSHARP: return "    Console.WriteLine(value);";
            case GO: return "    fmt.Println(value)";
            case KOTLIN: return "    println(value)";
            case RUST: return "    println!(\"{}\", value);";
            case SWIFT: return "    print(value)";
            case DART: return "    print(value);";
            case PASCAL: return "    WriteLn(Value);";
            case BATCH: return "    echo %VALUE%";
            case PYTHON: return "    print(self.value)";
            case SHELL: return "    echo \"$VALUE\"";
            case RUBY: return "    puts value";
            case PERL: return "    print $value;";
            case LUA: return "    print(value)";
            case HASKELL: return "    print value";
            case R: return "    print(value)";
            case NEON: return "        host: localhost";
            case YAML: return "        port: 8080";
            case TOML: return "    port = 8080";
            case INI: return "    port=8080";
            case PROPERTIES: return "    server.port=8080";
            case DOCKERFILE: return "    RUN echo hello";
            case SQL: return "    WHERE active = 1";
            case LATTE: return "    {$title}";
            case HTML: return "    <span>Text</span>";
            case XML: return "    <item>Text</item>";
            case CSS: return "    overflow: hidden;";
            case TEX: return "    \\section{Úvod}";
            default: throw unsupported(language);
        }
    }

    public static String getBlockSample(PreviewLanguage language)
    {
        switch (language) {
            case PHP: return "    function test() {\n        run();\n    }";
            case JAVA: return "    void test() {\n        service.run();\n    }";
            case JAVASCRIPT: return "    function test() {\n        run();\n    }";
            case TYPESCRIPT: return "    function test(): void {\n        run();\n    }";
            case C: return "    if (ready) {\n        run();\n    }";
            case CPP: return "    if (ready) {\n        run();\n    }";
            case C_HEADER: return "    void open(void);\n    void close(void);";
            case CSHARP: return "    if (ready) {\n        Run();\n    }";
            case GO: return "    if ready {\n        run()\n    }";
            case KOTLIN: return "    if (ready) {\n        run()\n    }";
            case RUST: return "    if ready {\n        run();\n    }";
            case SWIFT: return "    if ready {\n        run()\n    }";
            case DART: return "    if (ready) {\n        run();\n    }";
            case PASCAL: return "    if Ready then\n        Run;";
            case BATCH: return "    set HOST=localhost\n    set PORT=8080";
            case PYTHON: return "    def display(self):\n        print(self.value)";
            case SHELL: return "    if test -n \"$VALUE\"; then\n        echo \"$VALUE\"\n    fi";
            case RUBY: return "    if ready\n        run\n    end";
            case PERL: return "    if ($ready) {\n        run();\n    }";
            case LUA: return "    if ready then\n        run()\n    end";
            case HASKELL: return "    if ready\n        then run\n        else wait";
            case R: return "    if (ready) {\n        run()\n    }";
            case NEON: return "    database:\n        host: localhost\n        port: 3306";
            case YAML: return "    server:\n        host: localhost\n        port: 8080";
            case TOML: return "    host = \"localhost\"\n    port = 8080";
            case INI: return "    host=localhost\n    port=8080";
            case PROPERTIES: return "    server.host=localhost\n    server.port=8080";
            case DOCKERFILE: return "    RUN echo prepare\n    RUN echo build";
            case SQL: return "    SELECT id\n    FROM users\n    WHERE active = 1";
            case LATTE: return "    <div>\n        {$title}\n    </div>";
            case HTML: return "    <div>\n        <span>Text</span>\n    </div>";
            case XML: return "    <items>\n        <item>Text</item>\n    </items>";
            case CSS: return "    overflow: hidden;\n    position: relative;";
            case TEX: return "    \\section{Úvod}\n    Text dokumentu.";
            default: throw unsupported(language);
        }
    }

    private static IllegalStateException unsupported(PreviewLanguage language)
    {
        return new IllegalStateException("Nepodporovaný jazyk náhledu: " + language);
    }
}
