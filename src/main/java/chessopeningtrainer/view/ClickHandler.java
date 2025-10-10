package chessopeningtrainer.view;

import chessopeningtrainer.entity.board.Position;
import chessopeningtrainer.service.RootService;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class ClickHandler {
    PromotionController promotionController;

    Position position1 = new Position(-1,0);
    Position position2 = new Position(-1,0);
    RootService rootService;
    Button[][] buttonArray = new Button[8][8];


}
