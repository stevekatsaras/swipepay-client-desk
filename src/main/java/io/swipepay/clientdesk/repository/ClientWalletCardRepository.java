package io.swipepay.clientdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.swipepay.clientdesk.domain.ClientWallet;
import io.swipepay.clientdesk.domain.ClientWalletCard;

public interface ClientWalletCardRepository extends JpaRepository<ClientWalletCard, Long> {
	
	Long countByClientWallet(ClientWallet clientWallet);
	
	ClientWalletCard findByClientWalletAndCode(ClientWallet clientWallet, String code);
	
	List<ClientWalletCard> findByClientWalletAndIsDefaultFalse(ClientWallet clientWallet);
	
	ClientWalletCard findByClientWalletAndIsDefaultTrue(ClientWallet clientWallet);
	
	List<ClientWalletCard> findByClientWalletOrderByIsDefaultDesc(ClientWallet clientWallet);
	
}