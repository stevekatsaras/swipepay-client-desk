package io.swipepay.clientdesk.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import io.swipepay.clientdesk.domain.BusinessEntity;
import io.swipepay.clientdesk.domain.Card;
import io.swipepay.clientdesk.domain.CardBin;
import io.swipepay.clientdesk.domain.Client;
import io.swipepay.clientdesk.domain.ClientBilling;
import io.swipepay.clientdesk.domain.ClientCard;
import io.swipepay.clientdesk.domain.ClientCrypto;
import io.swipepay.clientdesk.domain.ClientCurrency;
import io.swipepay.clientdesk.domain.ClientKey;
import io.swipepay.clientdesk.domain.ClientProfile;
import io.swipepay.clientdesk.domain.ClientUser;
import io.swipepay.clientdesk.domain.ClientWallet;
import io.swipepay.clientdesk.domain.ClientWalletCard;
import io.swipepay.clientdesk.domain.Country;
import io.swipepay.clientdesk.domain.Currency;
import io.swipepay.clientdesk.domain.Offer;
import io.swipepay.clientdesk.domain.Plan;
import io.swipepay.clientdesk.domain.enums.CardPattern;
import io.swipepay.clientdesk.domain.enums.CardType;
import io.swipepay.clientdesk.domain.enums.ClientBillingStatus;
import io.swipepay.clientdesk.domain.enums.ClientProfileStatus;
import io.swipepay.clientdesk.domain.enums.ClientStatus;
import io.swipepay.clientdesk.domain.enums.ClientUserRole;
import io.swipepay.clientdesk.domain.enums.DateTimePattern;
import io.swipepay.clientdesk.domain.enums.PlanCostType;
import io.swipepay.clientdesk.domain.enums.PlanType;
import io.swipepay.clientdesk.exception.SignupException;
import io.swipepay.clientdesk.form.SignupForm;
import io.swipepay.clientdesk.repository.BusinessEntityRepository;
import io.swipepay.clientdesk.repository.CardBinRepository;
import io.swipepay.clientdesk.repository.CardRepository;
import io.swipepay.clientdesk.repository.ClientBillingRepository;
import io.swipepay.clientdesk.repository.ClientCardRepository;
import io.swipepay.clientdesk.repository.ClientCryptoRepository;
import io.swipepay.clientdesk.repository.ClientCurrencyRepository;
import io.swipepay.clientdesk.repository.ClientKeyRepository;
import io.swipepay.clientdesk.repository.ClientRepository;
import io.swipepay.clientdesk.repository.ClientProfileRepository;
import io.swipepay.clientdesk.repository.ClientUserRepository;
import io.swipepay.clientdesk.repository.ClientWalletCardRepository;
import io.swipepay.clientdesk.repository.ClientWalletRepository;
import io.swipepay.clientdesk.repository.CountryRepository;
import io.swipepay.clientdesk.repository.CurrencyRepository;
import io.swipepay.clientdesk.repository.OfferRepository;
import io.swipepay.clientdesk.repository.PlanRepository;
import io.swipepay.clientdesk.support.CalendarSupport;
import io.swipepay.clientdesk.support.CardSupport;
import io.swipepay.clientdesk.support.NumberSupport;
import io.swipepay.clientdesk.support.PasswordSupport;
import io.swipepay.cryptoservicelib.client.CryptoServiceClient;
import io.swipepay.cryptoservicelib.dto.EncryptDataRequest;
import io.swipepay.cryptoservicelib.dto.EncryptDataResponse;
import io.swipepay.cryptoservicelib.dto.GenerateKeyRequest;
import io.swipepay.cryptoservicelib.dto.GenerateKeyResponse;

@Service
public class SignupService {
	@Autowired
	private BusinessEntityRepository businessEntityRepository;
	
	@Autowired
	private CalendarSupport calendarSupport;
	
	@Autowired
	private CardRepository cardRepository;
	
	@Autowired
	private CardBinRepository cardBinRepository;
	
	@Autowired
	private CardSupport cardSupport;
	
	@Autowired
	private ClientBillingRepository clientBillingRepository;
	
	@Autowired
	private ClientCardRepository clientCardRepository;
	
	@Autowired
	private ClientCurrencyRepository clientCurrencyRepository;
	
	@Autowired
	private ClientCryptoRepository clientCryptoRepository;
	
	@Autowired
	private ClientKeyRepository clientKeyRepository;
	
	@Autowired
	private ClientProfileRepository clientProfileRepository;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private ClientUserRepository clientUserRepository;
	
	@Autowired
	private ClientWalletRepository clientWalletRepository;
	
	@Autowired
	private ClientWalletCardRepository clientWalletCardRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private CryptoServiceClient cryptoServiceClient;
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Autowired
	private NumberSupport numberSupport;
	
	@Autowired
	private OfferRepository offerRepository;
	
	@Autowired
	private PasswordSupport passwordSupport;
	
	@Autowired
	private PlanRepository planRepository;
	
	@Transactional(readOnly = true)
	public void initClient(ModelMap modelMap) throws SignupException {
		try {
			modelMap.addAttribute("businessEntities", businessEntityRepository.findAllByOrderByNameAsc());
			modelMap.addAttribute("countries", countryRepository.findAllByOrderByNameAsc());
		}
		catch (Exception exception) {
			throw new SignupException("We are unable to get the signup metadata.", exception);
		}
	}
	
	@Transactional(readOnly = true)
	public void initPlan(ModelMap modelMap) throws SignupException {
		try {
			modelMap.addAttribute("offer", offerRepository.findByEnabled(true)); // enabled
			
			List<Plan> plans = planRepository.findByTypeAndCostTypeOrderByCostAsc(
					PlanType.Standard.name(), 
					PlanCostType.Paid.name());
			
			List<Map<String,String>> plansList = new ArrayList<Map<String,String>>();
			for (Plan plan : plans) {
				Currency currency = plan.getCurrency();
				
				Map<String,String> aPlan = new HashMap<String,String>();
				aPlan.put("id", Long.toString(plan.getId()));
				aPlan.put("cost", numberSupport.formatMoney(
						plan.getCost(), 
						currency.getIso3(), 
						currency.getDivisible()));
				aPlan.put("unit", plan.getUnit());
				aPlan.put("txnCap", Long.toString(plan.getTxnCap()));
				aPlan.put("txnCapExcessCost", numberSupport.formatMoney(
						plan.getTxnCapExcessCost(), 
						currency.getIso3(), 
						currency.getDivisible()));
				
				plansList.add(aPlan);
			}
			modelMap.addAttribute("plans", plansList);
		}
		catch (Exception exception) {
			throw new SignupException("We are unable to get the signup metadata.", exception);
		}
	}
	
	@Transactional(readOnly = true)
	public void initPayment(SignupForm signupForm, ModelMap modelMap) throws SignupException {
		try {
			modelMap.addAttribute("cards", cardRepository.findByTypeOrderByBrandAsc(CardType.credit.name()));
			modelMap.addAttribute("months", calendarSupport.listMonthsOfTheYear());
			modelMap.addAttribute("years", calendarSupport.listFutureYearsFromNow(15));
			
			Offer offer = offerRepository.findByEnabled(true); // enabled
			
			Plan plan = planRepository.findByTypeAndCostTypeAndId(
					PlanType.Standard.name(), 
					PlanCostType.Paid.name(), 
					Long.parseLong(signupForm.getPlanId()));
			
			LocalDate initialBillingDate = calculateInitialBillingDate(offer, plan);
			modelMap.addAttribute("initialBillingDate", DateTimeFormatter.ofPattern(
					DateTimePattern.dMMMyyyy.toString()).format(
							initialBillingDate));
		}
		catch (Exception exception) {
			throw new SignupException("We are unable to get the signup metadata.", exception);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public void signup(SignupForm signupForm, ModelMap modelMap) throws SignupException {
		try {
			BusinessEntity businessEntity = businessEntityRepository.getOne(Long.parseLong(signupForm.getBusinessEntityId()));				
			Country country = countryRepository.getOne(Long.parseLong(signupForm.getCountryId()));

			Offer offer = offerRepository.findByEnabled(true); // enabled
			
			Plan plan = planRepository.findByTypeAndCostTypeAndId(
					PlanType.Standard.name(), 
					PlanCostType.Paid.name(), 
					Long.parseLong(signupForm.getPlanId()));
			
			Client client = new Client(
					signupForm.getClientName(), 
					businessEntity, 
					signupForm.getBusinessEntityNumber(), 
					signupForm.getWebsite(), 
					signupForm.getAddress1(), 
					signupForm.getAddress2(), 
					signupForm.getCity(), 
					signupForm.getState(), 
					signupForm.getPostcode(), 
					ClientStatus.Active.name(), 
					"priv_" + UUID.randomUUID().toString().replaceAll("-", ""), 
					LocalDate.now(), 
					LocalDateTime.now(), 
					country, 
					plan);
		
			clientRepository.saveAndFlush(client);
			
			ClientKey publicClientKey = new ClientKey(
					"pub_" + UUID.randomUUID().toString().replaceAll("-", ""), 
					true, // enabled
					LocalDateTime.now(), 
					client);
		
			clientKeyRepository.saveAndFlush(publicClientKey);
			
			List<Card> cards = cardRepository.findByTypeOrderByBrandAsc(CardType.credit.name());
			for (Card card : cards) {
				ClientCard clientCard = new ClientCard(
						"crd_" + UUID.randomUUID().toString().replaceAll("-", ""),
						true, // enabled 
						LocalDateTime.now(), 
						client, 
						card);
				
				clientCardRepository.saveAndFlush(clientCard);
			}
			
			List<Currency> currencies = currencyRepository.findAllByOrderByIso3Asc();
			for (Currency currency : currencies) {
				ClientCurrency clientCurrency = new ClientCurrency(
						"cur_" + UUID.randomUUID().toString().replaceAll("-", ""),
						true, // enabled 
						LocalDateTime.now(),
						client, 
						currency);
				
				clientCurrencyRepository.saveAndFlush(clientCurrency);
			}
			
			ClientProfile clientProfile = new ClientProfile(
					"pro_" + UUID.randomUUID().toString().replaceAll("-", ""), 
					"Default profile", 
					true, // enabled
					ClientProfileStatus.Test.name(),
					LocalDateTime.now(), 
					client);
		
			clientProfileRepository.saveAndFlush(clientProfile);
			
			ClientUser clientUser = new ClientUser(
					signupForm.getEmailAddress(),
					signupForm.getFirstname(), 
					signupForm.getLastname(), 
					signupForm.getTelephone(), 
					signupForm.getMobile(), 
					passwordSupport.hash(signupForm.getPassword()), 
					true, // enabled
					false, // not expired
					false, // not locked
					false, // not password expired
					ClientUserRole.Admin.name(),
					LocalDateTime.now(),
					client);
			
			clientUserRepository.saveAndFlush(clientUser);
			
			GenerateKeyResponse generateKeyResponse = cryptoServiceClient.generateKey(new GenerateKeyRequest());
			
			ClientCrypto clientCrypto = new ClientCrypto(
					generateKeyResponse.getCipherDataKey(), 
					true, // enabled
					LocalDateTime.now(), 
					client);
			
			clientCryptoRepository.saveAndFlush(clientCrypto);
			
			ClientWallet clientWallet = new ClientWallet(
					"Default wallet", 
					client);
		
			clientWalletRepository.saveAndFlush(clientWallet);
		
			EncryptDataResponse encryptDataResponse = cryptoServiceClient.encrypt(new EncryptDataRequest(
					clientCrypto.getDataKey(), 
					signupForm.getCardNumber()));
			
			String bin = cardSupport.bin(signupForm.getCardNumber());
			String pan = cardSupport.mask(signupForm.getCardNumber(), CardPattern.X.name());
			
			CardBin cardBin = cardBinRepository.findByBin(bin);
			Card card = cardSupport.card((List<Card>) modelMap.get("cards"), signupForm.getCardNumber());
			
			ClientWalletCard clientWalletCard = new ClientWalletCard(
					"crd_" + UUID.randomUUID().toString().replaceAll("-", ""),
					signupForm.getCardHolderName(), 
					signupForm.getCardHolderEmail(), 
					encryptDataResponse.getCipherTextData(), 
					pan, 
					signupForm.getExpiryMonth(), 
					signupForm.getExpiryYear(), 
					card, 
					cardBin, 
					true, // enabled
					true, // default
					LocalDateTime.now(), 
					clientWallet, 
					clientCrypto);
		
			clientWalletCardRepository.saveAndFlush(clientWalletCard);
			
			LocalDate initialBillingDate = calculateInitialBillingDate(offer, plan);
			
			//TODO: might need to think about how we determine which credit card from their wallet
			//we use to charge them and how to reflect it in this table (client_billing)!
			
			ClientBilling clientBilling = new ClientBilling(
					"bil_" + UUID.randomUUID().toString().replaceAll("-", ""),
					LocalDate.now(), 
					initialBillingDate, 
					ClientBillingStatus.New.name(), 
					client, 
					plan);
			
			clientBillingRepository.saveAndFlush(clientBilling);
			
			//TODO: send signup email!
			
			System.out.println("Email:" + signupForm.getEmailAddress());
			System.out.println("Password:" + signupForm.getPassword());
		}
		catch (Exception exception) {
			System.out.println(exception);
			throw new SignupException("We are unable to complete your signup.", exception);
		}
	}
	
	private LocalDate calculateInitialBillingDate(Offer offer, Plan plan) {
		LocalDate initialBillingDate = LocalDate.now();
		
		if (offer != null) {
			initialBillingDate = initialBillingDate.plus(offer.getTime(), ChronoUnit.valueOf(offer.getUnit().toUpperCase() + "S"));
		}
		initialBillingDate = initialBillingDate.plus(1, ChronoUnit.valueOf(plan.getUnit().toUpperCase() + "S"));
		return initialBillingDate;
	}
}