package com.ykaancan.fintechadmindashboard.service;

import com.ykaancan.fintechadmindashboard.dto.common.PagedResponse;
import com.ykaancan.fintechadmindashboard.dto.portfolio.CreatePortfolioRequest;
import com.ykaancan.fintechadmindashboard.dto.portfolio.PortfolioResponse;
import com.ykaancan.fintechadmindashboard.dto.portfolio.UpdatePortfolioRequest;
import com.ykaancan.fintechadmindashboard.entity.Portfolio;
import com.ykaancan.fintechadmindashboard.entity.User;
import com.ykaancan.fintechadmindashboard.exception.ResourceNotFoundException;
import com.ykaancan.fintechadmindashboard.repository.PortfolioRepository;
import com.ykaancan.fintechadmindashboard.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    public PortfolioService(PortfolioRepository portfolioRepository, UserRepository userRepository) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PortfolioResponse createPortfolio(CreatePortfolioRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.userId()));

        Portfolio portfolio = Portfolio.builder()
                .user(user)
                .name(request.name())
                .description(request.description())
                .build();

        return mapToResponse(portfolioRepository.save(portfolio));
    }

    @Transactional(readOnly = true)
    public PortfolioResponse getPortfolioById(UUID id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", id));
        return mapToResponse(portfolio);
    }

    @Transactional(readOnly = true)
    public PagedResponse<PortfolioResponse> getAllPortfolios(Pageable pageable) {
        Page<Portfolio> page = portfolioRepository.findAll(pageable);
        return PagedResponse.from(page, page.getContent().stream().map(this::mapToResponse).toList());
    }

    @Transactional(readOnly = true)
    public PagedResponse<PortfolioResponse> getPortfoliosByUserId(UUID userId, Pageable pageable) {
        Page<Portfolio> page = portfolioRepository.findByUserId(userId, pageable);
        return PagedResponse.from(page, page.getContent().stream().map(this::mapToResponse).toList());
    }

    @Transactional
    public PortfolioResponse updatePortfolio(UUID id, UpdatePortfolioRequest request) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", id));

        if (request.name() != null) portfolio.setName(request.name());
        if (request.description() != null) portfolio.setDescription(request.description());

        return mapToResponse(portfolioRepository.save(portfolio));
    }

    @Transactional
    public void deletePortfolio(UUID id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", id));
        portfolioRepository.delete(portfolio);
    }

    private PortfolioResponse mapToResponse(Portfolio portfolio) {
        User user = portfolio.getUser();
        return new PortfolioResponse(
                portfolio.getId(),
                user.getId(),
                user.getFirstName() + " " + user.getLastName(),
                portfolio.getName(),
                portfolio.getDescription(),
                portfolio.getCreatedAt(),
                portfolio.getUpdatedAt()
        );
    }
}
