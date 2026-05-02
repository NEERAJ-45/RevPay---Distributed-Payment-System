package com.neeraj.upi.wallet.controller;

import com.neeraj.upi.common.dto.ApiResponse;
import com.neeraj.upi.wallet.dto.AddMoneyRequest;
import com.neeraj.upi.wallet.dto.TransferRequest;
import com.neeraj.upi.wallet.dto.WalletResponse;
import com.neeraj.upi.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
@Tag(name = "Wallet", description = "Balance, top-up, and internal transfer")
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/balance/{upiId}")
    @Operation(summary = "Get wallet balance for a UPI ID")
    public ResponseEntity<ApiResponse<WalletResponse>> getBalance(@PathVariable String upiId) {
        // TODO: return walletService.getBalance(upiId)
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PostMapping("/add-money/{upiId}")
    @Operation(summary = "Mock bank top-up — credit wallet directly")
    public ResponseEntity<ApiResponse<WalletResponse>> addMoney(
            @PathVariable String upiId,
            @Valid @RequestBody AddMoneyRequest request) {
        // TODO: return walletService.addMoney(upiId, request)
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * INTERNAL ONLY — not exposed via API Gateway.
     * Called by transaction-service OpenFeign client.
     */
    @PostMapping("/internal/transfer")
    @Operation(summary = "[INTERNAL] Atomic debit/credit between two wallets")
    public ResponseEntity<ApiResponse<Void>> transfer(@Valid @RequestBody TransferRequest request) {
        // TODO: walletService.transfer(request), return 200 OK
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping("/ledger/{upiId}")
    @Operation(summary = "Get paginated ledger (transaction history) for a wallet")
    public ResponseEntity<ApiResponse<?>> getLedger(
            @PathVariable String upiId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: return paginated LedgerEntry list mapped to LedgerResponse DTOs
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
