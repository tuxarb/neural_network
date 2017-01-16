package app;

import app.net.NeuralNet;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.*;

public class Source {
    public static final Random RANDOM = new Random();
    private static final Scanner SCANNER;
    private static final NeuralNet NET;
    private static final String PATH_TO_FILE = "D:/weights.txt";

    static {
        SCANNER = new Scanner(System.in);
        SCANNER.useLocale(Locale.US);
        NET = new NeuralNet();
        NET.setCount(
                Config.FIRST_HIDDEN_LAYER_NEURONS_COUNT,
                Config.SECOND_HIDDEN_LAYER_NEURONS_COUNT
        ); //инициализация списков нейронов 1-го, 2-го скрытого слоев, а также выходного нейрона и установка степени нейрона и числа входящих ребер для каждого из нейронов
        NET.setRandomWeights(
                Config.LEFT_WEIGHT_VALUE,
                Config.RIGHT_WEIGHT_VALUE
        ); //устанавливаем рандомные веса [0.01, 0.02] для всех ребер между нейронами 1-го и 2-го слоев, а также нейронами 2-го и выходного слоев
        NET.setTeachCoeff(Config.TEACH_COEFFICIENT); //устанавливаем коэффициент обучения
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("1. Обучение");
            System.out.println("2. Проверка");
            System.out.print("Введите: ");

            int action = SCANNER.nextInt();
            switch (action) {
                case 1:
                    teach();
                    break;
                case 2:
                    check();
                    break;
                default:
                    System.out.println("Повторите ввод снова!");
                    break;
            }
            System.out.println();
        }
    }

    /**
     * Обучение сети
     */
    private static void teach() {
        NeuralNet.numberIterations = Config.ITERATIONS_COUNT; //количество итераций обучения
        System.out.println(LocalTime.now() + ": обучение сети началось.");

        for (int i = 0; i < NeuralNet.numberIterations; i++) {
            double x1 = getRandomX();
            double x2 = getRandomX();
            NET.teach(x1, x2, f(x1, x2));
            double result = NET.calculate();
            //System.out.println("итерация=" + i + "\tвыходное значение= " + result + "\tреальное значение= " + f(x1, x2));
        }
        System.out.println(LocalTime.now() + ": обучение сети закончилось.");

        writeToFile();
    }

    /**
     * Запись в файл всех весов в программе после последней итерации обучения
     */
    private static void writeToFile() {
        try (FileWriter writer = new FileWriter(new File(PATH_TO_FILE), false)) {
            List<Double> alpha;
            List<Double> beta;
            for (int neuron = 0; neuron < NET.getNeuronsLayer2().size(); neuron++) {
                alpha = NET.getNeuronsLayer2().get(neuron).getAlpha();
                beta = NET.getNeuronsLayer2().get(neuron).getBeta();
                saveWeightsForLastIteration(alpha, beta, writer);
            }
            alpha = NET.getOutput().getAlpha();
            beta = NET.getOutput().getBeta();
            saveWeightsForLastIteration(alpha, beta, writer);
            System.out.println("Файл с весами последней итерации обучения был сохранен в {" + PATH_TO_FILE + "}");
        } catch (IOException e) {
            System.out.println("Не могу сохранить файл в {" + PATH_TO_FILE + "} для записи результирующих весов." +
                    " Обучение не завершено!");
            System.exit(-1);
        }
    }

    private static void saveWeightsForLastIteration(List<Double> alpha, List<Double> beta, FileWriter writer) throws IOException {
        for (double alphaWeight : alpha) {
            writer.write(String.valueOf(alphaWeight) + "\n");
        }
        for (double betaWeight : beta) {
            writer.write(String.valueOf(betaWeight) + "\n");
        }
    }

    /**
     * Метод проверки обученной сети
     */
    private static void check() {
        try {
            List<String> weights = getAllWeightsFromFile(PATH_TO_FILE);

            final List<Double> alpha = fillAlphaList(weights, NET.getNeuronsLayer1().size());
            clearListBySize(weights, NET.getNeuronsLayer1().size());
            final List<Double> beta = fillBetaList(weights, NET.getNeuronsLayer1().size());
            clearListBySize(weights, NET.getNeuronsLayer1().size());
            NET.getNeuronsLayer2().forEach(neuron -> {
                neuron.setAlpha(alpha);
                neuron.setBeta(beta);
            });

            final List<Double> alphaOutput = fillAlphaList(weights, NET.getNeuronsLayer2().size());
            clearListBySize(weights, NET.getNeuronsLayer2().size());
            final List<Double> betaOutput = fillBetaList(weights, NET.getNeuronsLayer2().size());
            NET.getOutput().setAlpha(alphaOutput);
            NET.getOutput().setBeta(betaOutput);
        } catch (IOException e) {
            System.out.println("Файл {" + PATH_TO_FILE + "} не существует. Не могу произвести считывание весов.");
            System.exit(-1);
        } catch (Exception e) {
            System.out.println("Файл содержит неверное количество весов для рассматриваемой нейронной сети.");
            return;
        }

        double x1 = getX("Введите x1: ");
        double x2 = getX("Введите x2: ");
        NET.setInput(x1, x2);
        System.out.println("Выходное значение: " + NET.calculate());
        System.out.println("Реальное значение: " + f(x1, x2));
    }

    private static double getX(String message) {
        double x;
        while (true) {
            try {
                System.out.print(message);
                x = SCANNER.nextDouble();
                break;
            } catch (Exception e) {
                System.out.println("Неверный ввод. Попробуйте снова.");
                SCANNER.nextLine();
            }
        }
        return x;
    }

    /**
     * Считывание из файла всех весов с последней итерации обучения
     */
    private static List<String> getAllWeightsFromFile(String path) throws IOException {
        return Files.readAllLines(new File(path).toPath());
    }

    /**
     * Заполнение списка весов alpha для каждого из нейронов 2-го скрытого и выходного слоев
     */
    private static List<Double> fillAlphaList(List<String> allWeights, int endSize) {
        List<Double> alpha = new ArrayList<>();
        for (int i = 0; i < endSize; i++) {
            alpha.add(Double.valueOf(allWeights.get(i)));
        }
        return alpha;
    }

    /**
     * Заполнение списка весов beta для каждого из нейронов 2-го скрытого и выходного слоев
     */
    private static List<Double> fillBetaList(List<String> allWeights, int endSize) {
        List<Double> beta = new ArrayList<>();
        for (int i = 0; i < endSize; i++) {
            beta.add(Double.valueOf(allWeights.get(i)));
        }
        return beta;
    }

    private static void clearListBySize(List<String> allWeights, int size) {
        for (int i = 0; i < size; i++) {
            allWeights.remove(0);
        }
    }

    private static double getRandomX() {
        double x = RANDOM.nextDouble() % 100_000 + 1;
        x /= 10_000;
        return x; // x принадлежит [0, 0,0002]
    }

    private static double f(double x1, double x2) {
        return f1(x1) * f2(x2);
    }

    private static double f1(double x) {
        return x + 2;
    }

    private static double f2(double x) {
        return 2 * x - 3;
    }
}
