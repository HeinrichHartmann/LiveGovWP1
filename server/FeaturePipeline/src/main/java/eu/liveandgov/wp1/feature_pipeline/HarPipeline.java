package eu.liveandgov.wp1.feature_pipeline;

import eu.liveandgov.wp1.feature_pipeline.connectors.Consumer;
import eu.liveandgov.wp1.feature_pipeline.connectors.Pipeline;
import eu.liveandgov.wp1.feature_pipeline.containers.MotionSensorValue;
import eu.liveandgov.wp1.feature_pipeline.producers.ClassifyProducer;
import eu.liveandgov.wp1.feature_pipeline.producers.FeatureProducer;
import eu.liveandgov.wp1.feature_pipeline.producers.WindowProducer;

/**
 * Pipeline class that consumes accelerometer values and produces an activity stream.
 *
 * Created by hartmann on 10/20/13.
 */
public class HarPipeline extends Pipeline<MotionSensorValue, String> {

    private final WindowProducer windowProducer;
    private final FeatureProducer featureProducer;
    private final ClassifyProducer classifyProducer;

    public HarPipeline(){
        // Window
        windowProducer = new WindowProducer(5000, 200);

        // Feature
        featureProducer = new FeatureProducer();
        windowProducer.setConsumer(featureProducer);

        // Classify
        classifyProducer = new ClassifyProducer();
        featureProducer.setConsumer(classifyProducer);
    }

    public void push(MotionSensorValue message) {
        windowProducer.push(message);
    }

    @Override
    public void setConsumer(Consumer<String> c) {
        classifyProducer.setConsumer(c);
    }

}
