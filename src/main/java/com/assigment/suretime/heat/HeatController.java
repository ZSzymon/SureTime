package com.assigment.suretime.heat;


import com.assigment.suretime.generics.IGenericController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/heats")
public class HeatController implements IGenericController {

    @Autowired
    HeatService heatService;

    @GetMapping("{id}")
    public ResponseEntity<?> one(@PathVariable String id){
        return heatService.getOne(id);
    }
    @GetMapping
    public CollectionModel<?> all(){
        return heatService.getAll();
    }


    @PostMapping
    public ResponseEntity<?> addOne(@RequestBody Heat heat){
        return heatService.addOne(heat);
    }

    @PutMapping
    public ResponseEntity<?> updateOne(@RequestBody Heat heat){
        return heatService.updateOne(heat);
    }
}
