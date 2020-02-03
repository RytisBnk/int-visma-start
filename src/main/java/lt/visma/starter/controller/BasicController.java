package lt.visma.starter.controller;

import lt.visma.starter.service.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sample")
public class BasicController {
    private BasicService basicService;

    @Autowired
    public BasicController(BasicService basicService) {
        this.basicService = basicService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getSampleText() {
        return basicService.getData();
    }

    @RequestMapping(method = RequestMethod.POST)
    public String writeSampleData(@RequestBody String data) {
        return basicService.writeData(data);
    }
}
