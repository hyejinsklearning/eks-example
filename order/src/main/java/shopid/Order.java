package shopid;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Order_table")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String productId;
    private Integer qty;

    @PostPersist
    public void onPostPersist(){
        Ordered ordered = new Ordered();
        BeanUtils.copyProperties(this, ordered);
        ordered.publishAfterCommit();


    }

//    @PostRemove
//    public void onPostRemove(){
//        OrderCanceled orderCanceled = new OrderCanceled();
//        BeanUtils.copyProperties(this, orderCanceled);
//        orderCanceled.publishAfterCommit();
//
//        //Following code causes dependency to external APIs
//        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.
//
//        // external cancellation 으로 수정함
//        shopid.external.Cancellation cancellation = new shopid.external.Cancellation();
//        // mappings goes here
//        cancellation.setOrderId(String.valueOf(this.getId()));
//        cancellation.setStatus("CANCELED");
//        Application.applicationContext.getBean(shopid.external.CancellationService.class)
//            .cancel(cancellation);
//
//
//    }

//    @PrePersist
//    public void onPrePersist(){
//        Ordered ordered = new Ordered();
//        BeanUtils.copyProperties(this, ordered);
//        ordered.publishAfterCommit();
//
//
//    }

    @PreRemove
    public void onPreRemove(){
        OrderCanceled orderCanceled = new OrderCanceled();
        BeanUtils.copyProperties(this, orderCanceled);
        orderCanceled.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        shopid.external.Cancellation cancellation = new shopid.external.Cancellation();
        // mappings goes here
        cancellation.setOrderId(String.valueOf(this.getId()));
        cancellation.setStatus("CANCELED");
        Application.applicationContext.getBean(shopid.external.CancellationService.class)
            .cancel(cancellation);


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }




}
