package uet.k59t.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uet.k59t.controller.dto.StaffDTO;
import uet.k59t.controller.dto.StaffDTOwithPositionAndUnitname;
import uet.k59t.controller.dto.UnitDTO;
import uet.k59t.model.Staff;
import uet.k59t.model.Unit;
import uet.k59t.repository.UnitRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longlaptop on 12/3/2017.
 */
@RestController
public class UnitController {
    @Autowired
    private UnitRepository unitRepository;

    @RequestMapping(value = "unit/showall", method = RequestMethod.GET)
    public List<UnitDTO> showAllUnits(){
        List<Unit> unitList = (ArrayList<Unit>) unitRepository.findAll();
        List<UnitDTO> unitDTOList = new ArrayList<UnitDTO>();
        for (int i = 0; i < unitList.size(); i++) {
            UnitDTO unitDTO = new UnitDTO();
            Unit unitBeingIndexed = unitList.get(i);
            unitDTO.setUnitId(unitBeingIndexed.getUnitId());
            unitDTO.setUnitName(unitBeingIndexed.getUnitName());
            List<Staff> staffDTOList = unitBeingIndexed.getStaffList();
            List<StaffDTOwithPositionAndUnitname> staffDTOwithPositionAndUnitnames = new ArrayList<>();
            for (int j = 0; j < staffDTOList.size(); j++) {
                StaffDTOwithPositionAndUnitname staff1 = new StaffDTOwithPositionAndUnitname();
                staff1.setStaffName(staffDTOList.get(j).getStaffName());
                staff1.setUnitName(staffDTOList.get(j).getUnit().getUnitName());
                staff1.setPositionName(staffDTOList.get(j).getPosition().getPositionName());
                staffDTOwithPositionAndUnitnames.add(staff1);
            }
            unitDTO.setStaffList(staffDTOwithPositionAndUnitnames);
            unitDTOList.add(unitDTO);
        }
        return unitDTOList;
    }
}
