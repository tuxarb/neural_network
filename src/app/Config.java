package app;

/**
 * Created by Dmitry.
 */
class Config {
    // Коэффициент обучения
    static final double TEACH_COEFFICIENT = 0.0001;

    // Количество нейронов первого скрытого слоя
    static final int FIRST_HIDDEN_LAYER_NEURONS_COUNT = 10;
    // Количество нейронов второго скрытого слоя
    static final int SECOND_HIDDEN_LAYER_NEURONS_COUNT = 1;

    // Границы для выбора случайных весов
    static final double LEFT_WEIGHT_VALUE = 0.01;
    static final double RIGHT_WEIGHT_VALUE = 0.1;

    // Число итераций для обучения нейронной сети
    static final int ITERATIONS_COUNT = 500_000;

}
