package constants;

/**
 * Created by cao_y on 2018/1/22.
 */
public class SearchConstant {

    private static String NS = "http://www.sim.whu.edu.cn/historyevent.owl";

    public enum TargetModel {
        EVENT("event", "prefix event: <http://www.sim.whu.edu.cn/historyevent.owl#> ",
                ModelConstant.ModelNames.SANGUO_EVENT),
        FIGURE("figure", "prefix figure: <http://www.sim.whu.edu.cn/historyfigure.owl#> ",
                ModelConstant.ModelNames.SANGUO_FIGURE),
        LOCATION("location", "prefix location: <http://www.sim.whu.edu.cn/historylocation.owl#> ",
                ModelConstant.ModelNames.SANGUO_LOCATION),
        TIME("time", "prefix time: <http://www.sim.whu.edu.cn/historytime.owl#> ",
                ModelConstant.ModelNames.SANGUO_TIME);

        private String name;
        private String prefix;
        private ModelConstant.ModelNames modelName;

        private TargetModel(String name, String prefix, ModelConstant.ModelNames modelName) {
            this.name = name;
            this.prefix = prefix;
            this.modelName = modelName;
        }

        public String getName() {
            return this.name;
        }

        public String getPrefix() {
            return this.prefix;
        }

        public ModelConstant.ModelNames getModelName() {
            return this.modelName;
        }
    }
}
