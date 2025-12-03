using System;
using System.Globalization;
using System.IO;
using System.Text.RegularExpressions;

class BattleCodeArena
{
    // Arrays para almacenar datos de los estudiantes y su estado en roles
    static string[]? estudiantes;
    static int[]? puntuaciones; //Puntuación de cada estudiante
    static string[]? rangos;    //Rango basado en puntos

    static string[]? habilidadesEspeciales; //Habilidades si son seleccionadas
    static string? habilidadActualGuerrero;
    static int habilidadIndexActualGuerrero;
    static bool fueConHabilidad;
    static bool fueConSanador;
    static bool huboMaldicion;

    static int invokerIndex = -1;
    static int guerreroIndex = -1;

    static int[]? probabilidadesHabilidad;
    static string[]? rarezasHabilidad;

    static string? sanadorActual;
    static int sanadorIndexActual;
    static string retoActual = "";



    static bool[]? fueMaldecido; //Si fue afectado por una maldición

    static bool[]? yaSalioComoInvoker;  // true si ya fue seleccionado como Invoker
    static bool[]? yaSalioComoGuerrero; // true si ya fue seleccionado como Guerrero
    static bool[]? yaFueSanador;

    static bool[]? yaSalioComoSanador; // true si ya fue seleccionado como Sanador
    static string ultimoSeleccionadoComoSanador = "";

    static int tiempoExtra = 0; // Se usará para controlar si el guerrero gana tiempo adicional



    // Generador de numeros aleatorios para seleccion
    static Random random = new Random();

    // Guardar los ultimos seleccionados para mostrar o usar luego
    static string ultimoSeleccionadoComoInvoker = "";
    static string ultimoSeleccionadoComoGuerrero = "";

    static string[] frases = {
                "¡El tiempo corre, Guerrero!",
                "Cada segundo cuenta en esta batalla.",
                "La victoria está a un código de distancia.",
                "No te rindas, el reto es tuyo.",
                "El código es tu arma, úsalo sabiamente."
            };

#region Funciones de Carga
    static void Main()
    {
        CargarListaEstudiantes();
        CargarHabilidades(); // Cargar habilidades especiales de los estudiantes

        // Validamos que haya al menos 2 participantes para poder seleccionar
        if (estudiantes == null || estudiantes.Length < 2)
        {
            Console.WriteLine("No hay suficientes participantes para iniciar la batalla.");
            return; // Termina el programa si no hay suficientes estudiantes
        }

        MostrarMenu(); // Mostrar el menu interactivo al usuario

    }

    static void CargarListaEstudiantes()
    {
        try
        {
            // Ruta del archivo con la lista de estudiantes
            string rutaArchivo = "estudiantes.txt";
            if (!File.Exists(rutaArchivo))
            {
                Console.WriteLine("El archivo de estudiantes no existe.");
                Environment.Exit(1); // Termina el programa si no existe el archivo
            }

            string[] lineas = File.ReadAllLines(rutaArchivo);
            int cantidad = lineas.Length;

            //variables con informacion de los estudiantes
            estudiantes = new string[cantidad];
            puntuaciones = new int[cantidad];
            rangos = new string[cantidad];
            yaSalioComoInvoker = new bool[cantidad];
            yaSalioComoGuerrero = new bool[cantidad];
            fueMaldecido = new bool[cantidad];

            for (int i = 0; i < cantidad; i++)
            {
                string[] partes = lineas[i].Split(','); //Divide la informacion de cada linea del archivo (nombre y puntuacion)

                // Validamos que haya dos partes: nombre y puntuación
                if (partes.Length >= 1)
                {
                    estudiantes[i] = partes[0].Trim();
                    puntuaciones[i] = int.Parse(partes[1].Trim());
                    rangos[i] = AsignarRango(puntuaciones[i]); // función que haremos después segun cambien los puntos al avanzar
                }
                else
                {
                    Console.WriteLine($"Línea inválida en el archivo: {lineas[i]}");
                    Environment.Exit(1);
                }
            }

            Console.WriteLine("Lista de participantes fue cargada correctamente.");
        }
        catch (Exception ex)
        {
            // Si ocurre algun error, lo mostramos y cerramos el programa
            Console.WriteLine("Error al cargar los estudiantes: " + ex.Message);
            Environment.Exit(1);
        }
    }
#endregion

#region Menu Principal
    static void MostrarMenu()
    {
        bool continuar = true;

        while (continuar)
        {
            // Menu para el usuario
            Console.Clear();
            Console.ForegroundColor = ConsoleColor.DarkRed;
            Console.WriteLine(@"    ____  ___  ______________    ______   __________  ____  ______   ___    ____  _______   _____       ");
            Console.WriteLine(@"   / __ )/   |/_  __/_  __/ /   / ____/  / ____/ __ \/ __ \/ ____/  /   |  / __ \/ ____/ | / /   |      ");
            Console.WriteLine(@"  / __  / /| | / /   / / / /   / __/    / /   / / / / / / / __/    / /| | / /_/ / __/ /  |/ / /| |      ");
            Console.WriteLine(@" / /_/ / ___ |/ /   / / / /___/ /___   / /___/ /_/ / /_/ / /___   / ___ |/ _, _/ /___/ /|  / ___ |      ");
            Console.WriteLine(@"/_____/_/  |_/_/   /_/ /_____/_____/   \____/\____/_____/_____/  /_/  |_/_/ |_/_____/_/ |_/_/  |_|      ");
            Console.WriteLine();
            Console.ResetColor();
            Thread.Sleep(250); //animacion de entrada

            Console.ForegroundColor = ConsoleColor.Gray;
            Console.WriteLine("\nLos ecos del código resuenan en la arena...");
            Thread.Sleep(500);
            Console.WriteLine("Solo los más dignos podrán sobrevivir la batalla que se avecina.\n");
            Thread.Sleep(500);
            Console.ResetColor();


            Console.ForegroundColor = ConsoleColor.DarkGray;
            Console.WriteLine("\n╔════════════════════════════════════════════════════╗");
            Console.ResetColor();

            Console.ForegroundColor = ConsoleColor.DarkCyan;
            Console.WriteLine("1. Ver tutorial del BattleCode Arena");
            Thread.Sleep(100);

            Console.ForegroundColor = ConsoleColor.Green;
            Console.WriteLine("2. Invocar una nueva batalla");
            Thread.Sleep(100);

            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.WriteLine("3. Ver a los últimos elegidos por el código");
            Thread.Sleep(100);

            Console.ForegroundColor = ConsoleColor.Magenta;
            Console.WriteLine("4. Consultar el registro de batallas");
            Thread.Sleep(100);

            Console.ForegroundColor = ConsoleColor.Blue;
            Console.WriteLine("5. Ver el estado de los guerreros");
            Thread.Sleep(100);

            Console.ForegroundColor = ConsoleColor.DarkYellow;
            Console.WriteLine("6. Ver el ranking final de la arena");
            Thread.Sleep(100);

            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine("7. Abandonar la arena");
            Thread.Sleep(100);

            Console.ForegroundColor = ConsoleColor.DarkGray;
            Console.WriteLine("╚════════════════════════════════════════════════════╝");
            Console.ResetColor();


            Console.ForegroundColor = ConsoleColor.White;
            Console.Write("\n> ");
            Thread.Sleep(100);
            Console.Write("Invoca tu elección (1-7): ");
            Console.ResetColor();



            string? opcion = Console.ReadLine();
            switch (opcion)
            {
                case "1":
                    MostrarTutorial();
                    break;

                case "2":
                    IniciarSeleccion(); // Este elige aleatoriamente dos estudiantes y asignar roles
                    break;

                case "3":
                    VerUltimosSeleccionados(); // Este muestra quiénes fueron los últimos elegidos
                    break;

                case "4":
                    MostrarHistorial(); // Este muestra el historial guardado de selecciones
                    break;

                case "5":
                    MostrarEstadoJugadores(); // Este muestra el estado actual de los jugadores
                    break;

                case "6":
                    MostrarRankingFinal();
                    break;

                case "7":
                    bool salir = !ConfirmarSalida();  // Si devuelve false, quiere salir
                    if (salir)
                    {
                        Console.Clear();
                        Console.ForegroundColor = ConsoleColor.Red;
                        Console.WriteLine("==================================");
                        GuardarPuntuaciones(); // Guardamos las puntuaciones antes de salir
                        GuardarRankingEnArchivo(); // Guardamos el ranking final en un archivo

                        Console.WriteLine(" ¡Gracias por jugar en BattleCode Arena!");
                        Console.WriteLine(" ¡Hasta la próxima!");
                        Console.WriteLine("Saliendo del programa...");
                        Console.WriteLine("==================================");
                        Environment.Exit(0);
                    }
                    else
                    {
                        Console.WriteLine("Regresando al menú principal.");
                    }
                    break;

                default: // Este caso maneja opciones no válidas
                    Console.WriteLine("¡Los dioses del código no entienden esa invocación! Usa solo los números del 1 al 6.");
                    break;
            }
        }
    }

    static void MostrarTutorial()
    {
        void Seccion(string titulo, string contenido, ConsoleColor color)
        {
        Console.Clear();
        Console.ForegroundColor = ConsoleColor.DarkCyan;
        Console.WriteLine(@"  ______      __             _       __");
        Console.WriteLine(@" /_  __/_  __/ /_____  _____(_)___ _/ /");
        Console.WriteLine(@"  / / / / / / __/ __ \/ ___/ / __ `/ / ");
        Console.WriteLine(@" / / / /_/ / /_/ /_/ / /  / / /_/ / /  ");
        Console.WriteLine(@"/_/  \__,_/\__/\____/_/  /_/\__,_/_/   ");
        Console.ResetColor();

            Console.ForegroundColor = color;
            Console.WriteLine($"\n▸ {titulo.ToUpper()}");
            Console.ResetColor();
            Console.ForegroundColor = ConsoleColor.DarkGray;
            Console.WriteLine(contenido);
            EsperarYLimpiar();
        }

        Seccion("El origen del BattleCode Arena",
        "Mucho antes de que se forjaran los lenguajes de programación… \n" +
        "incluso antes de que los compiladores dieran forma al mundo digital… existió el Caos.\n" +
        "\n" +
        "Durante eras olvidadas, las ideas vagaban sin lógica. Los errores eran monstruos salvajes que devoraban el código naciente,\n" +
        "y la humanidad observaba en silencio el abismo binario.\n" +
        "Fue entonces cuando surgieron los Primeros Invokers: sabios que domaban los lenguajes \n" +
        "y sellaban sus lógicas en artefactos llamados 'retos'.\n" +
        "A su lado, emergieron los Guerreros del Teclado, almas valientes dispuestas a ejecutar código en tiempo real,\n" +
        "sin temor al juicio de los dioses del sistema.\n" +
        "\n" +
        "Pero toda estructura conlleva fragilidad. De las sombras de las cuevas mas alejadas nació la Maldición del Código Roto,\n" +
        "una fuerza digital que ataca al azar, despojando a los guerreros de sus dones.\n" +
        "\n" +
        "Y así fue como se erigió el BattleCode Arena. Un campo ancestral donde cada batalla no solo prueba habilidad técnica,\n" +
        " sino el temple, la estrategia y la resistencia mental de quienes pisan su terreno.\n" +
        "\n" +
        "Aquí, los nombres no se escriben...\n" +
        "Se compilan en la eternidad.",
        ConsoleColor.DarkCyan);


        Seccion("La llamada al rol",
            "Cada duelo convoca aleatoriamente a tres figuras clave:\n" +
            "El Invoker — (Facilitador de codigo) sabio portador del desafío. Forja el reto y define el tiempo.\n" +
            "El Guerrero — (Desarrollador de codigo) combatiente del teclado, debe ejecutar el reto ante todos.\n" +
            "El Sanador — invocado si el Guerrero lo desea. Solo puede ayudar UNA vez.\n" +
            "\nHay dos roles que nunca cambian: Los Dioses del Código y La Entidad Suprema.\n" +
            "Los Dioses del codigo es la manera para referirse al codigo mismo,\n" +
            " y la Entidad Suprema es la manera de referirnos al maestro o monitores.",
            ConsoleColor.Yellow);

        Seccion("Las bendiciones ocultas",
            "Durante la selección, el Guerrero puede elegir entre dos caminos:\n" +
            "🞄 Confiar en sus habilidades y pedir un poder especial.\n" +
            "🞄 Invocar a un Sanador para apoyarlo en un momento de crisis.\n\n" +
            "Pero cuidado... los dioses del código suelen ser caprichosos.",
            ConsoleColor.Green);

        Seccion("La Maldición del Caos",
            "Los Dioses aveces quieren entretenerse a costa de las batallas, por eso,\n" +
            "aveces la oscuridad digital desciende sobre la Arena...\n" +
            "\nSi el Guerrero es maldecido, perderá su habilidad o sanador.\n" +
            "Y deberá luchar solo, con ayuda únicamente del Invoker, al menos hasta donde soporte su nivel mental.\n\n" +
            "Si sobrevive... ganará el doble de gloria.\n" +
            "Pero si cae, su derrota será recordada como un ejemplo de lo que sucede al desafiar a los dioses.",
            ConsoleColor.Red);

        Seccion("Códigos de honor: puntuación",
            "Cada batalla es una prueba de habilidad y estrategia la cual es recompensda con puntuación.\n" +
            "Los puntos se otorgan así:\n" +
            "\nGuerrero:\n" +
            "  +15 si gana sin ayuda\n" +
            "  +10 si usa su habilidad o sanador\n" +
            "  x2 si es maldecido y sobrevive\n\n" +
            "Invoker:\n" +
            "  +10 si su Guerrero gana\n" +
            "  +5 si su Guerrero pierde\n\n" +
            "Sanador:\n" +
            "  +5 si ayuda y el Guerrero gana\n" +
            "  +2 si ayuda pero aún así pierde",
            ConsoleColor.Blue);

        Seccion("Los Rangos de la Arena",
            "Según tus puntos, tu nombre será recordado y tallado en piedras eternas con estos títulos:\n" +
            " Maestro del Código (90+ puntos)\n" +
            " Experto en Algoritmos (75+)\n" +
            " Programador Avanzado (50+)\n" +
            " Desarrollador Novato (30+)\n" +
            " Aprendiz de Programación (menos de 30)" +
            "\n\nCada rango otorga un color especial a tu nombre en la Arena, reflejando tu habilidad y dedicación." +
            "\nRecuerda que los rangos no son solo un título, son un símbolo de tu viaje en el mundo del código." +
            "\n¿Qué rango aspiras alcanzar?",
            ConsoleColor.DarkYellow);

        Seccion("Reglas sagradas",
            "Obviamente en nuestra arena hay reglas que debes seguir:\n" +
            "Usa solo las teclas correspondientes para responder a las preguntas del sistema.\n" +
            "   O podrias hacer enfadar a los dioses" +
            "Escribe los nombres exactamente como estan en la lista\n" +
            "   No uses apodos o abreviaciones, los dioses no lo tolerarán.\n" +
            "Los colores no son decoración: indican rareza, rol o peligro." +
            "Nunca ignores un mensaje en rojo, es una advertencia de los dioses.\n" +
            "   Si lo haces, podrías enfrentar consecuencias severas.\n" +
            "Y nunca pero nunca digas a la entidad suprema que no sabes algo.\n" +
            "   Eso es un pecado imperdonable en la Arena.",
            ConsoleColor.DarkRed);

        Console.ForegroundColor = ConsoleColor.Magenta;
        Console.WriteLine("\n══════════════════════════════════════════════════════════════════════");
        Console.WriteLine("  ¡Que el código te sea favorable, Participante! Prepárate para la arena.");
        Console.WriteLine("══════════════════════════════════════════════════════════════════════");
        Console.ResetColor();

        EsperarYLimpiar();
        Console.Clear();
    }

    
    static bool ConfirmarSalida()
    {
        Console.Write("¿Estás seguro de que deseas salir? (s/n): ");
        string? respuesta = Console.ReadLine();
        respuesta = respuesta?.Trim().ToLower(CultureInfo.InvariantCulture) ?? "";

        // Confirmación estricta para evitar salidas accidentales
        if (respuesta == "s")
        {
            Console.Clear();
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine("\n==================================");
            GuardarPuntuaciones();
            Console.WriteLine(" ¡Gracias por jugar en BattleCode Arena!");
            Console.WriteLine(" ¡Hasta la próxima!");
            Console.WriteLine("==================================");
            Console.ResetColor();

            GuardarRankingEnArchivo();
            return false; // indica salir del programa (salir del while)
        }
        else
        {
            Console.WriteLine(" Volviendo al menú...");
            return true; // continuar en el menú
        }

    }
#endregion

#region Funciones de Batalla y Selección
    static void IniciarSeleccion()
    {
        Console.Clear();
        Console.ForegroundColor = ConsoleColor.DarkRed;
        Console.WriteLine("\n  ¡Las puertas de la arena se abren con estruendo!");
        Console.WriteLine($"Los antiguos observan... y el código elige a sus campeones.");
        Console.ResetColor();

        Console.ForegroundColor = ConsoleColor.Blue;
        Console.WriteLine(@"    _   __                         ____        __        ____     ");
        Console.WriteLine(@"   / | / /_  _____ _   ______ _   / __ )____ _/ /_____ _/ / /___ _");
        Console.WriteLine(@"  /  |/ / / / / _ \ | / / __ `/  / __  / __ `/ __/ __ `/ / / __ `/");
        Console.WriteLine(@" / /|  / /_/ /  __/ |/ / /_/ /  / /_/ / /_/ / /_/ /_/ / / / /_/ / ");
        Console.WriteLine(@"/_/ |_/\__,_/\___/|___/\__,_/  /_____/\__,_/\__/\__,_/_/_/\__,_/  ");
        Console.ResetColor();

        Console.ForegroundColor = ConsoleColor.Green;
        Console.WriteLine("\n\n========================================");
        Console.WriteLine("  INICIANDO SELECCIÓN DE PARTICIPANTES  ");
        Console.WriteLine("========================================");
        Console.ResetColor();
        // Validaciones para evitar errores
        if (estudiantes == null || estudiantes.Length < 2)
        {
            Console.WriteLine("No hay suficientes participantes para seleccionar.");
            return;
        }

        // Si los arrays de control son nulos, los inicializamos
        if (yaSalioComoInvoker == null || yaSalioComoGuerrero == null)
        {
            yaSalioComoInvoker = new bool[estudiantes.Length];
            yaSalioComoGuerrero = new bool[estudiantes.Length];
        }

        // Verificamos que todavía haya estudiantes disponibles para el rol Invoker y por descarte el guerrro
        if (yaSalioComoInvoker != null && TodosYaFueron(yaSalioComoInvoker))
        {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine("No hay suficientes participantes para seleccionar.");
            Console.ResetColor();
            return;
        }

        if (yaSalioComoInvoker == null || yaSalioComoInvoker.Length != estudiantes.Length)
        {
            yaSalioComoInvoker = new bool[estudiantes.Length];
        }

        invokerIndex = SeleccionarJugadorVisualmente("Invoker", yaSalioComoInvoker);
        ultimoSeleccionadoComoInvoker = estudiantes[invokerIndex];

        if (yaSalioComoGuerrero == null || yaSalioComoGuerrero.Length != estudiantes.Length)
        {
            yaSalioComoGuerrero = new bool[estudiantes.Length];
        }
        guerreroIndex = SeleccionarJugadorVisualmente("Guerrero", yaSalioComoGuerrero, new int[] { invokerIndex });
        ultimoSeleccionadoComoGuerrero = estudiantes[guerreroIndex];

        // Mostrar los seleccionados con colores para distinguir roles
        Console.ForegroundColor = ConsoleColor.Cyan;
        Console.WriteLine($"\n Invoker del Código: {ultimoSeleccionadoComoInvoker}");
        Thread.Sleep(250);

        Console.ForegroundColor = ConsoleColor.Yellow;
        Console.WriteLine($" Guerrero del Teclado: {ultimoSeleccionadoComoGuerrero}");
        Thread.Sleep(250);

        Console.ResetColor();
        Console.WriteLine("\nSelección completada. ¡Que comience la batalla!");

        EsperarYLimpiar();

        PedirRetoAInvoker(ultimoSeleccionadoComoInvoker);

        Console.ResetColor();

        //Aqui el guerrero decide si quiere una habilidad especial o un sanador
        Console.ForegroundColor = ConsoleColor.DarkRed;
        Console.WriteLine("\nGuerrero " + ultimoSeleccionadoComoGuerrero + ", tu destino está por definirse...");
        Thread.Sleep(250);

        string? decision;
        do
        {
            Console.ForegroundColor = ConsoleColor.DarkRed;
            Console.Write("¿Prefieres invocar un Sanador (s) o recibir una Habilidad Especial (h)? ");
            Console.ResetColor();
            decision = Console.ReadLine()?.Trim().ToLower();

            if (decision != "s" && decision != "h")
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("Los dioses no reconocen esa respuesta. Solo se aceptan 's' o 'h'.");
                Console.WriteLine($"Intenta de nuevo, Guerrero {ultimoSeleccionadoComoGuerrero}.");
                Console.ResetColor();
                Thread.Sleep(200);
            }
        } while (decision != "s" && decision != "h");


        if (decision == "h")
        {
            MostrarInvocacion("Invocando poder");
            fueConHabilidad = true;
            if (habilidadesEspeciales != null && habilidadesEspeciales.Length > 0)
            {
                int indiceHabilidad = random.Next(habilidadesEspeciales.Length);
                string habilidadAsignada = habilidadesEspeciales[indiceHabilidad];

                // Mostrar la habilidad con color según rareza
                string rareza = (rarezasHabilidad != null) ? rarezasHabilidad[indiceHabilidad] : "común";
                switch (rareza)
                {
                    case "común":
                        Console.ForegroundColor = ConsoleColor.Gray;
                        break;
                    case "rara":
                        Console.ForegroundColor = ConsoleColor.Blue;
                        break;
                    case "legendaria":
                        Console.ForegroundColor = ConsoleColor.Magenta;
                        break;
                    default:
                        Console.ResetColor();
                        break;
                }

                Console.WriteLine($"\n¡Has recibido la habilidad especial: {habilidadAsignada}!");
                Console.ResetColor();


                Console.Write("¿Deseas que se te explique esta habilidad? (s/n): ");
                string? quiereExplicacion = Console.ReadLine()?.Trim().ToLower();

                if (quiereExplicacion == "s")
                {
                    MostrarExplicacionDeHabilidad(indiceHabilidad);
                }

                // Guardamos la habilidad para usarla después ya que es necesaria para el temporizador
                habilidadActualGuerrero = habilidadAsignada;
                habilidadIndexActualGuerrero = indiceHabilidad;
                AplicarEfectoDeHabilidad(habilidadIndexActualGuerrero);

                Console.ForegroundColor = ConsoleColor.Green;
                Console.WriteLine("¡Habilidad especial asignada correctamente!");
                Console.WriteLine("Recuerda que puedes usar esta habilidad UNA sola vez en un momento critico de la batalla.");
                Console.ResetColor();
                EsperarYLimpiar();

            }
            else
            {
                Console.WriteLine("No hay habilidades disponibles en este momento.");
            }
        }
        else if (decision == "s")
        {
            fueConSanador = true;
            if (estudiantes.Length <= 2) // Si hay 2 o menos estudiantes, no se puede asignar un sanador
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("No hay suficientes participantes para invocar un sanador.");
                Console.ResetColor();
            }
            else
            {

                Console.ForegroundColor = ConsoleColor.Green;
                Console.WriteLine("\n El Invoker del Código, el Maestro de la logica o la maquina, te puede asignar un sanadorr.");
                Console.WriteLine("Prefieres que se te asigne manualmente o aleatoriamente? (m/a): ");
                Console.ResetColor();
                string? tipoSanador = Console.ReadLine()?.Trim().ToLower();

                if (tipoSanador == "m")
                {
                    if (yaFueSanador == null || yaFueSanador.Length != estudiantes.Length)
                        yaFueSanador = new bool[estudiantes.Length];

                    while (true)
                    {
                        Console.Write("Ingresa el nombre del sanador que deseas asignar: ");
                        string? nombreSanador = Console.ReadLine()?.Trim();

                        // Buscar índice del estudiante
                        int sanadorManualIndex = Array.FindIndex(estudiantes, e =>
                            e.Equals(nombreSanador, StringComparison.OrdinalIgnoreCase));

                        // Validaciones
                        if (sanadorManualIndex == -1)
                        {
                            Console.ForegroundColor = ConsoleColor.Red;
                            Console.WriteLine(" Ese nombre no está en la lista de estudiantes.");
                            Console.ResetColor();
                            continue;
                        }

                        if (sanadorManualIndex == invokerIndex || sanadorManualIndex == guerreroIndex)
                        {
                            Console.ForegroundColor = ConsoleColor.Red;
                            Console.WriteLine(" No puedes elegir al Invoker ni al Guerrero como Sanador.");
                            Console.ResetColor();
                            continue;
                        }

                        if (yaFueSanador[sanadorManualIndex])
                        {
                            Console.ForegroundColor = ConsoleColor.Red;
                            Console.WriteLine(" Ese estudiante ya ha sido asignado como Sanador.");
                            Console.ResetColor();
                            continue;
                        }

                        // Si todo está bien, asignamos
                        yaFueSanador[sanadorManualIndex] = true;
                        sanadorActual = estudiantes[sanadorManualIndex];

                        Console.ForegroundColor = ConsoleColor.Green;
                        Console.WriteLine($" Sanador asignado: {sanadorActual}");
                        Console.WriteLine("Recuerda que puedes usarlo en momentos críticos de la batalla. Su poder es limitado.");
                        Console.ResetColor();
                        EsperarYLimpiar();

                        break; // salimos del while
                    }
                }

                else if (tipoSanador == "a")
                {
                    // Inicializar yaFueSanador si es necesario
                    if (yaFueSanador == null || yaFueSanador.Length != estudiantes.Length)
                    {
                        yaFueSanador = new bool[estudiantes.Length];
                    }

                    // Verificar si hay al menos un sanador válido
                    bool haySanadorDisponible = false;
                    for (int i = 0; i < estudiantes.Length; i++)
                    {
                        if (i != invokerIndex && i != guerreroIndex && !yaFueSanador[i])
                        {
                            haySanadorDisponible = true;
                            break;
                        }
                    }

                    if (!haySanadorDisponible)
                    {
                        Console.ForegroundColor = ConsoleColor.DarkRed;
                        Console.WriteLine("\nLos dioses del código no encontraron sanadores disponibles.");
                        Console.WriteLine("Deberás enfrentar esta batalla solo con la guía del Invoker.");
                        EsperarYLimpiar();

                    }
                    else
                    {
                        int sanadorIndex;

                        if (yaSalioComoSanador == null || yaSalioComoSanador.Length != estudiantes.Length)
                        {
                            yaSalioComoSanador = new bool[estudiantes.Length];
                        }
                        sanadorIndex = SeleccionarJugadorVisualmente("Sanador", yaSalioComoSanador, new int[] { invokerIndex, guerreroIndex });
                        ultimoSeleccionadoComoSanador = estudiantes[sanadorIndex];

                        string sanador = estudiantes[sanadorIndex];
                        sanadorActual = sanador;
                        yaFueSanador[sanadorIndex] = true;

                        Console.ForegroundColor = ConsoleColor.Green;
                        Console.WriteLine($"\n{sanador} ha sido convocado como tu Sanador.");
                        Console.WriteLine("Recuerda que puedes usarlo en momentos críticos de la batalla, pero su poder es limitado.");
                        Console.ResetColor();
                        EsperarYLimpiar();

                    }
                }

                else
                {
                    Console.WriteLine("Selección no válida. Se te asignará un Sanador por defecto.");
                }

            }
        }
        GuardarSeleccionEnHistorial(invoker: ultimoSeleccionadoComoInvoker, guerrero: ultimoSeleccionadoComoGuerrero);

        Console.Clear();
        Console.ForegroundColor = ConsoleColor.Gray;
        Console.WriteLine("\nLa energía mágica comienza a fluctuar en la arena...");
        Thread.Sleep(1200);
        Console.WriteLine("Los cielos digitales se oscurecen ligeramente...");
        Thread.Sleep(1200);
        Console.WriteLine("El destino de esta batalla está siendo evaluado por las antiguas fuerzas del código...");
        Thread.Sleep(1500);
        Console.ResetColor();

        bool estaMaldito = AplicarMaldicion();

        if (estaMaldito)
        {
            AnimacionMaldicion();
            Thread.Sleep(5000);
            Console.Clear();
            PedirTiempoAInvoker(ultimoSeleccionadoComoInvoker, ultimoSeleccionadoComoGuerrero);
        }
        else
        {
            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine("\n Una luz brillante te envuelve...");
            Console.WriteLine($"¡Los dioses del código han escuchado tu deseo {ultimoSeleccionadoComoGuerrero}!");
            Console.WriteLine("Puedes usar lo que has elegido.");
            Console.ResetColor();

            Thread.Sleep(5000);
            Console.Clear();
            PedirTiempoAInvoker(ultimoSeleccionadoComoInvoker, ultimoSeleccionadoComoGuerrero);
        }

        if (huboMaldicion == true)
        {
            ChecklistDeBatalla(fueConSanador, fueConHabilidad, huboMaldicion);
        }

        PedirTiempoAInvoker(ultimoSeleccionadoComoInvoker, ultimoSeleccionadoComoGuerrero);
        GuardarSeleccionEnHistorial(ultimoSeleccionadoComoInvoker, ultimoSeleccionadoComoGuerrero);
        ChecklistDeBatalla(fueConSanador, fueConHabilidad, huboMaldicion);
    }
    

    static bool AplicarMaldicion()
    {
        double probabilidad = 0.3; // 30% de probabilidad de maldición
        return random.NextDouble() < probabilidad;

    }


    static void CargarHabilidades()
    {

        try
        {
            string rutaArchivo = "habilidades.txt";
            if (!File.Exists(rutaArchivo))
            {
                Console.WriteLine("El archivo de habilidades no existe.");
                Environment.Exit(1);
            }

            string[] lineas = File.ReadAllLines(rutaArchivo);
            habilidadesEspeciales = new string[lineas.Length];
            probabilidadesHabilidad = new int[lineas.Length];
            rarezasHabilidad = new string[lineas.Length];

            for (int i = 0; i < lineas.Length; i++)
            {
                string[] partes = lineas[i].Split('|');
                if (partes.Length < 3)
                {
                    Console.WriteLine($"Formato inválido en la línea {i + 1} del archivo de habilidades.");
                    Environment.Exit(1);
                }

                habilidadesEspeciales[i] = partes[0].Trim();
                probabilidadesHabilidad[i] = int.Parse(partes[1].Trim());
                rarezasHabilidad[i] = partes[2].Trim().ToLower(); // común, rara o legendaria
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("Error al cargar las habilidades: " + ex.Message);
            Environment.Exit(1);
        }
        // Fin de CargarHabilidades
    }

    static void MostrarExplicacionDeHabilidad(int indice)
    {
        try
        {
            string[] explicaciones = File.ReadAllLines("habilidades_explicaciones.txt");

            if (indice < explicaciones.Length)
            {
                Console.ForegroundColor = ConsoleColor.Yellow;
                Console.WriteLine($"\nExplicación: {explicaciones[indice]}");
                Console.ResetColor();
            }
            else
            {
                Console.WriteLine("No se encontró una explicación para esta habilidad.");
            }
        }
        catch
        {
            Console.WriteLine("No se pudo cargar la explicación de la habilidad.");
        }
    }

    static void AplicarEfectoDeHabilidad(int indice)
    {
        switch (indice)
        {
            case 0: // Ataque lógico
                Console.WriteLine(" Puedes repetir un intento sin penalización. ¡Usa esta ventaja sabiamente!");
                // Lógica de repetición la podemos agregar más adelante si es necesario
                break;

            case 1: // Escudo contra errores de sintaxis
                Console.WriteLine(" Tus errores de sintaxis serán ignorados una vez.");
                break;

            case 2: // Visión de depuración
                Console.WriteLine(" Tienes 15 segundos para leer el código del Invoker.");
                break;

            case 3: // Teletransporte de código
                Console.WriteLine(" Tienes la opcion de intercambiar por 10 segundos con alguien de la arena.");
                break;

            case 4: // Reloj de sol (15s extra)
                Console.WriteLine(" Ganas 15 segundos extra en tu tiempo.");
                tiempoExtra += 15;
                break;

            case 5: // Reloj de arena (30s extra)
                Console.WriteLine(" Ganas 30 segundos extra en tu tiempo.");
                tiempoExtra += 30;
                break;

            case 6: // Transformación en pseudocódigo
                Console.WriteLine(" Puedes transformar lógica a pseudocódigo para explicarlo.");
                break;

            case 7: // Lenguaje universal
                Console.WriteLine(" Puedes usar cualquier lenguaje de programación durante tu turno.");
                break;

            case 8: // Poder divino
                Console.WriteLine(" Puedes hacer una pregunta al profesor o monitor sin penalización.");
                break;

            case 9: // Memoria expandida
                Console.WriteLine(" Los primeros dos errores el invoker debe señalarlos sin penalización.");
                break;

            case 10: // Bloqueo de bugs menores
                Console.WriteLine(" Ignoras errores lógicos menores.");
                break;

            case 11: // Absorción de comentarios
                Console.WriteLine(" El Invoker debe leerte 2 comentarios importantes de su código.");
                break;

            case 12: // Modo fantasma
                Console.WriteLine(" Solo tú puedes hablar cuando pidas ayuda (y obviamente la persona a la que pidas ayuda). Silencio total en la arena.");
                break;

            case 13: // Conexión directa
                Console.WriteLine(" Puedes hacer UNA búsqueda en internet o usar una IA para UNA pergunta.");
                break;

            case 14: // Oído fino
                Console.WriteLine(" El Invoker debe explicarte al menos 3 lineas de codigo con palabras simples.");
                break;

            default:
                Console.WriteLine("No hay efecto definido para esta habilidad.");
                break;
        }
    }

    static void ChecklistDeBatalla(bool fueConSanador, bool fueConHabilidad, bool huboMaldicion)
    {
        //Console.Clear();
        Console.ForegroundColor = ConsoleColor.Blue;
        Console.WriteLine("\n==================================");
        Console.WriteLine("  EVALUACIÓN FINAL DE BATALLA");
        Console.WriteLine($"Guerrero: {ultimoSeleccionadoComoGuerrero}");
        Console.WriteLine($"Invoker: {ultimoSeleccionadoComoInvoker}");
        if (fueConSanador == false && huboMaldicion == false) Console.WriteLine($"Sanador: {sanadorActual}");
        Console.WriteLine("==================================");
        Console.ResetColor();

        // Preguntar si el guerrero sobrevivió
        string? respGano;
        bool gano;
        do
        {
            Console.Write("¿El Guerrero sobrevivió a la batalla? (s/n): ");
            respGano = Console.ReadLine()?.Trim().ToLower();

            if (respGano != "s" && respGano != "n")
            {
                Console.WriteLine("Los dioses del código no reconocen esa respuesta. Por favor, responde con 's' o 'n'.");
            }
        } while (respGano != "s" && respGano != "n");

        if (respGano == "s")
        {
            gano = true;
        }
        else
        {
            gano = false;
        }

        // Si hubo maldición, preguntar si la sobrevivió
        string? respMaldicion;
        bool sobrevivioMaldicion = false;

        if (gano == false && huboMaldicion == true)
        {
            ModificarPuntuacion(ultimoSeleccionadoComoGuerrero, 0);
            ModificarPuntuacion(ultimoSeleccionadoComoInvoker, 5);
        }

        else
        {
            if (huboMaldicion)
            {
                do
                {
                    Console.Write("¿El Guerrero sobrevivió a la maldición? (s/n): ");
                    respMaldicion = Console.ReadLine()?.Trim().ToLower();

                    if (respMaldicion != "s" && respMaldicion != "n")
                    {
                        Console.WriteLine("Los dioses del código no reconocen esa respuesta. Por favor, responde con 's' o 'n'.");
                    }
                } while (respMaldicion != "s" && respMaldicion != "n");

                // Asignar el valor booleano según la respuesta
                //sobrevivioMaldicion = Console.ReadLine()?.Trim().ToLower() == "s";
                if (respMaldicion == "s")
                {
                    sobrevivioMaldicion = true;
                }
                else
                {
                    sobrevivioMaldicion = false;
                }
            }
        }

        // Preguntar si se usó la habilidad o sanador
        string? respSanador;
        string? respHabilidad;
        bool fueUsado = false;
        if (fueConSanador && sobrevivioMaldicion == false)
        {
            do
            {
                Console.Write("¿El Sanador fue utilizado durante la batalla? (s/n): ");
                respSanador = Console.ReadLine()?.Trim().ToLower();

                if (respSanador != "s" && respSanador != "n")
                {
                    Console.WriteLine("Los dioses del código no reconocen esa respuesta. Por favor, responde con 's' o 'n'.");
                }
            } while (respSanador != "s" && respSanador != "n");

            fueUsado = respSanador == "s";
        }

        else if (fueConHabilidad && sobrevivioMaldicion == false)
        {
            do
            {
                Console.Write("¿El Sanador fue utilizado durante la batalla? (s/n): ");
                respSanador = Console.ReadLine()?.Trim().ToLower();

                if (respSanador != "s" && respSanador != "n")
                {
                    Console.WriteLine("Los dioses del código no reconocen esa respuesta. Por favor, responde con 's' o 'n'.");
                }
            } while (respSanador != "s" && respSanador != "n");

            fueUsado = respSanador == "s";
        }

        else if (fueConHabilidad)
        {
            do
            {
                Console.Write("¿La Habilidad fue utilizada durante la batalla? (s/n): ");
                respHabilidad = Console.ReadLine()?.Trim().ToLower();

                if (respHabilidad != "s" && respHabilidad != "n")
                {
                    Console.WriteLine("Los dioses del código no reconocen esa respuesta. Por favor, responde con 's' o 'n'.");
                }
            } while (respHabilidad != "s" && respHabilidad != "n");

            fueUsado = respHabilidad == "s";
        }

        else if (fueConSanador)
        {
            do
            {
                Console.Write("¿El Sanador fue utilizado durante la batalla? (s/n): ");
                respSanador = Console.ReadLine()?.Trim().ToLower();

                if (respSanador != "s" && respSanador != "n")
                {
                    Console.WriteLine("Los dioses del código no reconocen esa respuesta. Por favor, responde con 's' o 'n'.");
                }
            } while (respSanador != "s" && respSanador != "n");

            fueUsado = respSanador == "s";
        }

        // Calcular puntos
        int puntosGuerrero = 0;
        int puntosInvoker = gano ? 10 : 5;
        int puntosSanador = 0;

        if (gano)
        {
            puntosGuerrero = (!fueUsado) ? 15 : 10;
        }

        if (sobrevivioMaldicion == true)
        {
            puntosGuerrero *= 2;
            puntosInvoker *= 2;
        }

        if (fueConSanador && fueUsado)
        {
            puntosSanador = gano ? 5 : 2;
        }

        // Asignar los puntos
        ModificarPuntuacion(ultimoSeleccionadoComoGuerrero, puntosGuerrero);
        ModificarPuntuacion(ultimoSeleccionadoComoInvoker, puntosInvoker);
        if (fueConSanador && fueUsado && !string.IsNullOrEmpty(sanadorActual))
        {
            ModificarPuntuacion(sanadorActual, puntosSanador);
        }

        // Mostrar resultado
        Console.WriteLine("\n PUNTOS ASIGNADOS:");
        Console.ForegroundColor = ConsoleColor.Yellow;
        Console.WriteLine($"Guerrero: {puntosGuerrero}");
        Console.ForegroundColor = ConsoleColor.Cyan;
        Console.WriteLine($"Invoker: {puntosInvoker}");
        if (fueConSanador && fueUsado)
        {
            Console.ForegroundColor = ConsoleColor.Green;
            Console.WriteLine($"Sanador: {puntosSanador}");
        }
        Console.ResetColor();

        Console.WriteLine("\n¡Batalla finalizada! Los puntos han sido asignados.");
        EsperarYLimpiar();

        sanadorActual = null;
        sanadorIndexActual = -1;
        habilidadActualGuerrero = null;
        habilidadIndexActualGuerrero = -1;
        fueConSanador = false;
        fueConHabilidad = false;
        huboMaldicion = false;
        if (fueMaldecido != null)
        {
            for (int i = 0; i < fueMaldecido.Length; i++)
            {
                fueMaldecido[i] = false;
            }
        }

        Console.Clear();
        MostrarMenu();
    }

        static string PedirRetoAInvoker(string invoker)
    {
        Console.Clear();
        Console.ForegroundColor = ConsoleColor.DarkCyan;
        Console.WriteLine($"{invoker}, como Invoker del Código, es tu deber declarar el reto.");
        Console.WriteLine("Consulta con La Entidad Suprema (el maestro o monitor) si tienes dudas...");
        Console.ResetColor();

        Console.Write("\nIngresa el reto que el Guerrero debe cumplir: ");
        string? reto = Console.ReadLine()?.Trim();

        //No permitir reto vacío, letras solas o con caracteres inválidos
        while (string.IsNullOrWhiteSpace(reto) ||
               reto.Length == 1 ||
               !Regex.IsMatch(reto, @"^[a-zA-Z0-9\s\p{P}]+$"))
        {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine(" El reto no puede estar vacío o contener caracteres inválidos. Inténtalo de nuevo.");
            Console.ResetColor();
            Console.Write("\nIngresa el reto que el Guerrero debe cumplir: ");
            reto = Console.ReadLine()?.Trim();
        }

        Console.Clear();
        Console.ForegroundColor = ConsoleColor.DarkMagenta;
        Console.WriteLine(" RETO DECLARADO POR EL INVOKER");
        Console.ResetColor();

        retoActual = reto ?? "Reto no especificado"; // Aseguramos que nunca sea null

        Console.WriteLine($"\n El desafío impuesto por el Invoker {invoker} es:\n\"{reto}\"\n");
        return reto ?? string.Empty;
    }

    static int PedirTiempoAInvoker(string invoker, string guerrero)
    {
        Console.Clear();
        Console.ForegroundColor = ConsoleColor.Cyan;
        Console.WriteLine($"\n{invoker}, como Invoker debes establecer los minutos que tu Guerrero {guerrero} tendrá para completar el reto.");
        Console.WriteLine("Recuerda consultar con La Entidad Suprema si es necesario.");
        Console.ResetColor();

        Console.Write("\n Ingresa el tiempo en minutos: ");
        string? entrada = Console.ReadLine();
        int minutos;

        //si la entrada es nula o no es un número, pedir de nuevo
        while (string.IsNullOrWhiteSpace(entrada) || !int.TryParse(entrada, out minutos) || minutos < 0)
        {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine(" Entrada inválida. Debes ingresar un número entero positivo.");
            Console.ResetColor();
            Console.Write("\n Ingresa el tiempo en minutos: ");
            entrada = Console.ReadLine();
        }

        // Validar que el tiempo no sea mayor a 15 minutos
        if (minutos > 10)
        {
            Console.WriteLine("El tiempo máximo permitido es de 10 minutos. Se ajustará a 10 minutos.");
            minutos = 10;
        }

        int totalSegundos = minutos * 60 + tiempoExtra;
        SimularCronometro(totalSegundos);
        return totalSegundos;
    }

    static void SimularCronometro(int totalSegundos)
    {
        Console.Clear();
        Console.ForegroundColor = ConsoleColor.Green;
        int minutos = totalSegundos / 60;
        Console.WriteLine($"\nEl Guerrero tiene {minutos} minutos para ejecutar su misión...");
        Console.ResetColor();
        EsperarYLimpiar();

        Console.Clear();
        Console.ForegroundColor = ConsoleColor.DarkYellow;
        Console.WriteLine($"El invoker {ultimoSeleccionadoComoInvoker} ha declarado el reto: \"{retoActual}\"");
        Console.WriteLine($"¿Lograra nuestro guerrero {ultimoSeleccionadoComoGuerrero} completar el reto y vencer en la batalla?");
        Console.WriteLine("");
        Console.ForegroundColor = ConsoleColor.Magenta;
        if (fueConSanador)
        {
            Console.WriteLine($"El sanador {sanadorActual} está listo para ayudar si es necesario.");
        }
        else if (fueConHabilidad)
        {
            Console.WriteLine($"El guerrero {ultimoSeleccionadoComoGuerrero}, cuenta con la habilidad \"{habilidadActualGuerrero}\" para usar en la batalla.");
        }
        Console.WriteLine("");
        Console.ForegroundColor = ConsoleColor.Cyan;
        Console.WriteLine(frases[random.Next(frases.Length)]);
        Console.WriteLine();
        Console.ResetColor();



        for (int i = totalSegundos; i > 0; i--)
        {
            int minutosRestantes = i / 60;
            int segundosRestantes = i % 60;
            double porcentaje = (double)i / totalSegundos;

            if (porcentaje > 0.5)
            {
                Console.ForegroundColor = ConsoleColor.Green;
            }
            else if (porcentaje > 0.2)
            {
                Console.ForegroundColor = ConsoleColor.DarkBlue;
            }
            else
            {
                Console.ForegroundColor = ConsoleColor.Red;
            }

            Console.Write($"\rTiempo restante: {minutosRestantes:D2}:{segundosRestantes:D2} ");
            Thread.Sleep(1000);


            Thread.Sleep(1000);
        }

        Console.Clear();
        Console.ForegroundColor = ConsoleColor.Red;
        Console.WriteLine(@"    _______              __        __         ____        __        ____     ");
        Console.WriteLine(@"   / ____(_)___     ____/ /__     / /___ _   / __ )____ _/ /_____ _/ / /___ _");
        Console.WriteLine(@"  / /_  / / __ \   / __  / _ \   / / __ `/  / __  / __ `/ __/ __ `/ / / __ `/");
        Console.WriteLine(@" / __/ / / / / /  / /_/ /  __/  / / /_/ /  / /_/ / /_/ / /_/ /_/ / / / /_/ / ");
        Console.WriteLine(@"/_/   /_/_/ /_/   \__,_/\___/  /_/\__,_/  /_____/\__,_/\__/\__,_/_/_/\__,_/  ");
        Console.ResetColor();
        Console.ResetColor();
        Console.ForegroundColor = ConsoleColor.Yellow;
        Console.WriteLine("\nEl código será juzgado por La Entidad Suprema...");
        Thread.Sleep(1500);
        Console.WriteLine("El Invoker y el Guerrero serán evaluados según su desempeño...");
        Thread.Sleep(1500);
        Console.WriteLine("¿Obtendran una puntuacion digna... o su rango no tendra avance alguno?");
        Thread.Sleep(2000);

        ChecklistDeBatalla(fueConSanador, fueConHabilidad, huboMaldicion);

    }
    #endregion

    #region Archivo y Puntuaciones
    static void GuardarRankingEnArchivo()
    {
        try
        {
            string rutaArchivo = "ranking_final.txt";
            using (StreamWriter writer = new StreamWriter(rutaArchivo))
            {
                writer.WriteLine("   RANKING FINAL – BattleCode Arena    ");
                writer.WriteLine("---------------------------------------");

                if (estudiantes == null || puntuaciones == null || rangos == null)
                {
                    writer.WriteLine("No hay datos suficientes para mostrar el ranking.");
                    return;
                }

                int[] indices = new int[estudiantes.Length];
                for (int i = 0; i < indices.Length; i++)
                    indices[i] = i;

                Array.Sort(indices, (a, b) => puntuaciones[b].CompareTo(puntuaciones[a]));

                for (int i = 0; i < indices.Length; i++)
                {
                    int idx = indices[i];
                    writer.WriteLine($"{i + 1}. {estudiantes[idx]} - {puntuaciones[idx]} pts - {rangos[idx]}");
                }
            }

            Console.ForegroundColor = ConsoleColor.Green;
            Console.WriteLine("Ranking guardado correctamente.");
            Console.ResetColor();
        }

        catch (Exception ex)
        {
            Console.WriteLine("No se pudo guardar el ranking: " + ex.Message);
        }
   }

    static void GuardarPuntuaciones()
    {
        try
        {
            if (estudiantes == null || puntuaciones == null)
                return;

            using (StreamWriter writer = new StreamWriter("estudiantes.txt"))
            {
                for (int i = 0; i < estudiantes.Length; i++)
                {
                    writer.WriteLine($"{estudiantes[i]},{puntuaciones[i]}");
                }
            }

            Console.WriteLine("Puntuaciones guardadas correctamente.");
        }

        catch (Exception ex)
        {
            Console.WriteLine("Error al guardar las puntuaciones: " + ex.Message);
        }
    }

        static void ModificarPuntuacion(string nombreEstudiante, int cantidad)
    {
        if (puntuaciones == null || rangos == null)
            return;

        if (estudiantes == null)
            return;

        for (int i = 0; i < estudiantes.Length; i++)
        {
            if (estudiantes[i].Equals(nombreEstudiante, StringComparison.OrdinalIgnoreCase))
            {
                puntuaciones[i] += cantidad;
                rangos[i] = AsignarRango(puntuaciones[i]);
                break;
            }
        }
    }


    static void GuardarSeleccionEnHistorial(string invoker, string guerrero)
    {
        string rutaHistorial = "historial_selecciones.txt";

        string fecha = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");

        List<string> lineas = new List<string>
        {
            "══════════════════════════════════════════════",
            $"Fecha y hora: {fecha}",
            $"Reto Actual:{retoActual}",
            "",
            $"Invoker del Código:       {invoker}",
            $"Guerrero del Teclado:     {guerrero}"
        };

        if (fueConSanador && !string.IsNullOrEmpty(sanadorActual))
        {
            lineas.Add($"Sanador de Emergencia:    {sanadorActual}");
        }

        if (fueConHabilidad && habilidadActualGuerrero != null)
        {
            lineas.Add($"Habilidad Especial:       {habilidadActualGuerrero}");
        }



        lineas.Add("══════════════════════════════════════════════");

        try
        {
            File.AppendAllLines(rutaHistorial, lineas);
            File.AppendAllText(rutaHistorial, Environment.NewLine); // línea en blanco entre bloques
        }
        catch (Exception ex)
        {
            Console.WriteLine("No se pudo guardar en el historial: " + ex.Message);
        }
    }

    static void MostrarRankingFinal()
    {
        if (estudiantes == null || puntuaciones == null || rangos == null)
        {
            Console.WriteLine("No hay datos suficientes para mostrar el ranking.");
            return;
        }

        // Crear un array con los índices para ordenarlos
        int[] indices = new int[estudiantes.Length];
        for (int i = 0; i < indices.Length; i++)
            indices[i] = i;

        // Ordenamos los índices según la puntuación (de mayor a menor)
        Array.Sort(indices, (a, b) => puntuaciones[b].CompareTo(puntuaciones[a]));

        // Mostrar encabezado
        Console.Clear();
        Console.ForegroundColor = ConsoleColor.Yellow;
        Console.WriteLine(@"    ____              __   _                ");
        Console.WriteLine(@"   / __ \____ _____  / /__(_)___  ____ _    ");
        Console.WriteLine(@"  / /_/ / __ `/ __ \/ //_/ / __ \/ __ `/    ");
        Console.WriteLine(@" / _, _/ /_/ / / / / ,< / / / / / /_/ /     ");
        Console.WriteLine(@"/_/ |_|\__,_/_/ /_/_/|_/_/_/ /_/\__, /      ");
        Console.WriteLine(@"                               /____/       ");
        Console.ResetColor();

        for (int i = 0; i < indices.Length; i++)
        {
            int idx = indices[i];
            Console.ForegroundColor = ObtenerColorPorRango(rangos[idx]);
            Console.WriteLine($"{i + 1}. {estudiantes[idx]} - {puntuaciones[idx]} pts - {rangos[idx]}");
            Console.ResetColor();
        }

        EsperarYLimpiar();
        Console.Clear();
    }

    static void MostrarHistorial()
    {
        string rutaHistorial = "historial_selecciones.txt";

        if (!File.Exists(rutaHistorial))
        {
            Console.WriteLine(" No hay historial aún.");
            return;
        }

        Console.ForegroundColor = ConsoleColor.DarkYellow;
        Console.WriteLine(@"    ____             _      __                    __        ____        __        ____          ");
        Console.WriteLine(@"   / __ \___  ____ _(_)____/ /__________     ____/ /__     / __ )____ _/ /_____ _/ / /___ ______");
        Console.WriteLine(@"  / /_/ / _ \/ __ `/ / ___/ __/ ___/ __ \   / __  / _ \   / __  / __ `/ __/ __ `/ / / __ `/ ___/");
        Console.WriteLine(@" / _, _/  __/ /_/ / (__  ) /_/ /  / /_/ /  / /_/ /  __/  / /_/ / /_/ / /_/ /_/ / / / /_/ (__  ) ");
        Console.WriteLine(@"/_/ |_|\___/\__, /_/____/\__/_/   \____/   \__,_/\___/  /_____/\__,_/\__/\__,_/_/_/\__,_/____/  ");
        Console.WriteLine(@"           /____/                                                                                ");
        Console.ResetColor();

        string[] lineas = File.ReadAllLines(rutaHistorial);

        foreach (string linea in lineas)
        {
            if (linea.Contains("════════"))
            {
                Console.ForegroundColor = ConsoleColor.DarkGray;
                Console.WriteLine(linea);
            }
            else if (linea.StartsWith("Fecha y hora:"))
            {
                Console.ForegroundColor = ConsoleColor.Blue;
                Console.WriteLine(linea);
            }
            else if (linea.StartsWith("Reto Actual:"))
            {
                Console.ForegroundColor = ConsoleColor.Cyan;
                Console.WriteLine(linea);
            }
            else if (linea.StartsWith("Invoker del Código:"))
            {
                Console.ForegroundColor = ConsoleColor.Yellow;
                Console.WriteLine(linea);
            }
            else if (linea.StartsWith("Guerrero del Teclado:"))
            {
                Console.ForegroundColor = ConsoleColor.DarkYellow;
                Console.WriteLine(linea);
            }
            else if (linea.StartsWith("Sanador de Emergencia:"))
            {
                Console.ForegroundColor = ConsoleColor.Green;
                Console.WriteLine(linea);
            }
            else if (linea.StartsWith("Habilidad Especial:"))
            {
                Console.ForegroundColor = ConsoleColor.Magenta;
                Console.WriteLine(linea);
            }
            else
            {
                Console.ResetColor();
                Console.WriteLine(linea);
            }
        }


        EsperarYLimpiar();

    }
    #endregion

    #region Roles y Estado
    static void MostrarEstadoJugadores()
    {
        Console.Clear();
        Console.ForegroundColor = ConsoleColor.DarkMagenta;
        Console.WriteLine(@"    ______     __            __             __        ______                                         ");
        Console.WriteLine(@"   / ____/____/ /_____ _____/ /___     ____/ /__     / ____/_  _____  _____________  _________  _____");
        Console.WriteLine(@"  / __/ / ___/ __/ __ `/ __  / __ \   / __  / _ \   / / __/ / / / _ \/ ___/ ___/ _ \/ ___/ __ \/ ___/");
        Console.WriteLine(@" / /___(__  ) /_/ /_/ / /_/ / /_/ /  / /_/ /  __/  / /_/ / /_/ /  __/ /  / /  /  __/ /  / /_/ (__  ) ");
        Console.WriteLine(@"/_____/____/\__/\__,_/\__,_/\____/   \__,_/\___/   \____/\__,_/\___/_/  /_/   \___/_/   \____/____/  ");
        Console.ResetColor();

        if (estudiantes == null || puntuaciones == null || rangos == null)
        {
            Console.WriteLine("No hay datos de estudiantes para mostrar.");
            return;
        }

        while (true)
        {
            Console.ForegroundColor = ConsoleColor.DarkGray;
            Console.Write("\n¿Deseas ver el estado de todos los jugadores (t) o de uno específico (e)? ");
            string? eleccion = Console.ReadLine()?.Trim().ToLower();

            if (eleccion == "t")
            {
                Console.Clear();
                Console.ForegroundColor = ConsoleColor.Blue;
                Console.WriteLine("\n Estado de todos los jugadores:\n");
                // Mostrar el estado de todos los estudiantes
                for (int i = 0; i < estudiantes.Length; i++)
                {
                    Console.ForegroundColor = ConsoleColor.White;
                    Console.WriteLine("=======================================");
                    Console.ForegroundColor = ConsoleColor.Green;
                    Console.Write($"Jugador: {estudiantes[i]}");

                    Console.ForegroundColor = ConsoleColor.Yellow;
                    Console.Write($" | Puntos: {puntuaciones[i]}");

                    Console.ForegroundColor = ObtenerColorPorRango(rangos[i]);
                    Console.WriteLine($" | Rango: {rangos[i]}");
                    Console.ResetColor();
                    Thread.Sleep(200);
                }
                EsperarYLimpiar();
                break;
            }
            else if (eleccion == "e")
            {
                Console.Write("Escribe el nombre del estudiante que deseas consultar: ");
                string? nombreBuscado = Console.ReadLine()?.Trim();

                int indice = Array.FindIndex(estudiantes, e => e.Trim().Equals(nombreBuscado, StringComparison.OrdinalIgnoreCase));

                if (indice == -1)
                {
                    Console.ForegroundColor = ConsoleColor.Red;
                    Console.WriteLine("No se encontró ese nombre en la lista.");
                    Console.ResetColor();
                }
                else
                {
                    Console.ForegroundColor = ConsoleColor.Green;
                    //animacion de que esta buscando el estado de ese estudiante
                    Console.Write($"\n Buscando estado de {estudiantes[indice]}");
                    for (int i = 0; i < 8; i++)
                    {
                        Console.Write(".");
                        Thread.Sleep(200);
                    }
                    Console.ForegroundColor = ConsoleColor.DarkBlue;
                    Console.WriteLine($"\nEstado de {estudiantes[indice]} encontrado.");
                    EsperarYLimpiar();
                    MostrarEstadoIndividual(indice);
                    break;
                }
            }
            else
            {
                Console.WriteLine("Opción inválida. Intenta de nuevo.");
            }
        }
    }

    static void MostrarEstadoIndividual(int i)
    {
        if (estudiantes == null || puntuaciones == null || rangos == null)
        {
            Console.WriteLine("No hay datos disponibles para mostrar el estado del estudiante.");
            return;
        }

        Console.Clear();
        Console.WriteLine("=======================================");
        Console.ForegroundColor = ConsoleColor.White;
        Console.Write("Jugador: ");
        Console.ForegroundColor = ConsoleColor.Green;
        Console.WriteLine(estudiantes[i]);

        Console.ForegroundColor = ConsoleColor.White;
        Console.Write("Puntos: ");
        Console.ForegroundColor = ConsoleColor.Yellow;
        Console.WriteLine($"{puntuaciones[i]}");

        Console.ForegroundColor = ConsoleColor.White;
        Console.Write("Rango: ");
        Console.ForegroundColor = ObtenerColorPorRango(rangos[i]);
        Console.WriteLine($"{rangos[i]}");
        Console.ResetColor();
        Console.WriteLine("=======================================");

        EsperarYLimpiar();

    }

    static string AsignarRango(int puntos)
    {
        // Asignamos rangos basados en la puntuación
        if (puntos >= 90)
            return "Maestro del Código";
        else if (puntos >= 75)
            return "Experto en Algoritmos";
        else if (puntos >= 50)
            return "Programador Avanzado";
        else if (puntos >= 30)
            return "Desarrollador Novato";
        else
            return "Aprendiz de Programación";
    }

    

    static ConsoleColor ObtenerColorPorRango(string rango)
    {
        return rango switch
        {
            "Maestro del Código" => ConsoleColor.DarkBlue,
            "Experto en Algoritmos" => ConsoleColor.Cyan,
            "Programador Avanzado" => ConsoleColor.Yellow,
            "Desarrollador Novato" => ConsoleColor.Green,
            "Aprendiz de Programación" => ConsoleColor.Gray,
            _ => ConsoleColor.White
        };
    }
    #endregion

    #region Utilidades
    static int MostrarRuletaVisual(string[] lista, string rol, bool[]? filtro = null)
    {
        Console.ForegroundColor = ConsoleColor.DarkGray;
        Console.WriteLine($"\n Invocando al {rol} de esta batalla...");
        Thread.Sleep(500);

        int indexElegido = -1;
        // Simulación visual de la ruleta
        for (int i = 0; i < 20; i++)
        {
            int index;
            do
            {
                index = random.Next(lista.Length);
            } while (filtro != null && filtro[index]); // Evitar nombres ya seleccionados

            string nombre = lista[index];
            Console.Write($"\r Los dioses te han elegido a ti: {nombre.PadRight(40)}");
            Thread.Sleep(60 + i * 5); // velocidad decreciente
            indexElegido = index;
        }

        Console.ResetColor();
        Console.WriteLine(); // espacio extra
        Console.Clear();

        return indexElegido;
    }

    static void MostrarInvocacion(string mensaje)
    {
        string[] efectos = {
            "    .     ",
            "   ...    ",
            "  .....   ",
            " .......  ",
            "..........",
            "  *"+mensaje+"*  ",
            "..........",
            " .......  ",
            "  .....   ",
            "   ...    ",
            "    .     "
        };

        foreach (string linea in efectos)
        {
            Console.Clear();
            Console.ForegroundColor = ConsoleColor.DarkMagenta;
            Console.WriteLine(linea);
            Thread.Sleep(120);
        }
        Console.ResetColor();
    }

    static void AnimacionMaldicion()
    {
        string[] maldicion = {
            "Una sombra cae sobre la arena...",
            "Nubes oscuras cubren el cielo digital...",
            "El código comienza a corromperse...",
            "¡Has sido maldecido!"

        };

        foreach (var linea in maldicion)
        {
            Console.ForegroundColor = ConsoleColor.DarkMagenta;
            Console.WriteLine(linea);
            Thread.Sleep(700);
        }
        Console.ResetColor();
    }

    
    
    static int SeleccionarJugadorVisualmente(string rol, bool[] yaElegidos, int[]? indicesExcluidos = null)
    {
        if (estudiantes == null || yaElegidos == null)
            return -1;

        // Filtrar posibles candidatos
        List<int> indicesValidos = new List<int>();
        for (int i = 0; i < estudiantes.Length; i++)
        {
            if (!yaElegidos[i] && (indicesExcluidos == null || !indicesExcluidos.Contains(i)))
            {
                indicesValidos.Add(i);
            }
        }

        // Obtener nombres de los candidatos
        string[] candidatos = indicesValidos.Select(i => estudiantes[i]).ToArray();

        MostrarInvocacion($"Eligiendo al {rol}");
        int indexLocal = MostrarRuletaVisual(candidatos, rol);

        // Convertimos al índice real y marcamos como ya seleccionado
        int indiceReal = indicesValidos[indexLocal];
        yaElegidos[indiceReal] = true;

        return indiceReal;
    }

    static void EsperarYLimpiar()
    {
        Console.WriteLine("\nPresiona cualquier tecla para continuar...");
        Console.ReadKey();
        Console.Clear();
    }
    static void VerUltimosSeleccionados()
    {
        // Si aún no se ha hecho ninguna selección, avisa al usuario
        if (ultimoSeleccionadoComoInvoker == "" || ultimoSeleccionadoComoGuerrero == "")
        {
            Console.WriteLine("\nAún no se ha hecho ninguna selección.");
        }
        else
        {
            Console.ForegroundColor = ConsoleColor.DarkRed;
            Console.WriteLine(@"   __  ______  _                    _____      __               _           ");
            Console.WriteLine(@"  / / / / / /_(_)___ ___  ____ _   / ___/___  / /__  __________(_)___  ____ ");
            Console.WriteLine(@" / / / / / __/ / __ `__ \/ __ `/   \__ \/ _ \/ / _ \/ ___/ ___/ / __ \/ __ \");
            Console.WriteLine(@"/ /_/ / / /_/ / / / / / / /_/ /   ___/ /  __/ /  __/ /__/ /__/ / /_/ / / / /");
            Console.WriteLine(@"\____/_/\__/_/_/ /_/ /_/\__,_/   /____/\___/_/\___/\___/\___/_/\____/_/ /_/ ");
            Console.ResetColor();

            // Mostrar los últimos seleccionados con sus colores
            Console.WriteLine("\nÚltimos seleccionados:");

            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine($"Último Invoker: {ultimoSeleccionadoComoInvoker}");

            Console.ForegroundColor = ConsoleColor.DarkRed;
            Console.WriteLine($"Último Guerrero: {ultimoSeleccionadoComoGuerrero}");

            if (fueConSanador == true)
            {
                Console.WriteLine($"Último Sanador: {sanadorActual}");
            }

            // Si quieres también mostrar al sanador, puedes hacer esto:
            if (!string.IsNullOrEmpty(sanadorActual))
            {
                Console.ForegroundColor = ConsoleColor.Green;
                Console.WriteLine($"Sanador invocado: {sanadorActual}");
            }

            Console.ResetColor();
        }
    }

    static bool TodosYaFueron(bool[] arrayRol)
    {
        foreach (bool yaSalio in arrayRol)
        {
            if (!yaSalio)
                return false; // Aún hay alguien disponible
        }
        return true; // Todos ya salieron
    }
#endregion
}
