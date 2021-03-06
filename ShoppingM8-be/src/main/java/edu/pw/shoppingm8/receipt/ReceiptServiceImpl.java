package edu.pw.shoppingm8.receipt;

import edu.pw.shoppingm8.authentication.AuthenticationService;
import edu.pw.shoppingm8.list.List;
import edu.pw.shoppingm8.receipt.event.ReceiptCreatedEvent;
import edu.pw.shoppingm8.receipt.exception.ReceiptNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final AuthenticationService authenticationService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Collection<Receipt> getReceiptsByList(List list) {
        return receiptRepository.findByList(list);
    }

    @Override
    public Receipt getReceipt(List list, Long id) {
        return receiptRepository.findByListAndId(list, id)
                .orElseThrow(ReceiptNotFoundException::new);
    }

    @Override
    public Receipt createReceipt(byte[] receiptPicture, List list) {
        Receipt receipt = Receipt.builder()
                .list(list)
                .createdBy(authenticationService.getAuthenticatedUser())
                .picture(receiptPicture)
                .build();
        receiptRepository.save(receipt);
        eventPublisher.publishEvent(new ReceiptCreatedEvent(receipt));
        return receipt;
    }
}
