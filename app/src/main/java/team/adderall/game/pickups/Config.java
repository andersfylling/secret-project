package team.adderall.game.pickups;

import addy.annotations.Service;
import addy.annotations.ServiceLinker;

@Service
@ServiceLinker({
        BuffsHandler.class
})
public class Config {}
